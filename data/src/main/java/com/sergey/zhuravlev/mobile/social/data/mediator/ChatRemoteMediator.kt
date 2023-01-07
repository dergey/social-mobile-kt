package com.sergey.zhuravlev.mobile.social.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.sergey.zhuravlev.mobile.social.data.api.common.ErrorData
import com.sergey.zhuravlev.mobile.social.data.api.common.Sort
import com.sergey.zhuravlev.mobile.social.data.api.dto.ChatPreviewDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.PageDto
import com.sergey.zhuravlev.mobile.social.data.api.service.ChatService
import com.sergey.zhuravlev.mobile.social.data.database.AppDatabase
import com.sergey.zhuravlev.mobile.social.data.database.entity.ChatAndLastMessageEntities
import com.sergey.zhuravlev.mobile.social.data.database.entity.ChatEntity
import com.sergey.zhuravlev.mobile.social.data.database.entity.MessageEntity
import com.sergey.zhuravlev.mobile.social.data.database.pageable.Pageable
import com.sergey.zhuravlev.mobile.social.data.mapper.ChatEntityMapper
import com.sergey.zhuravlev.mobile.social.data.mapper.MessageEntityMapper
import com.sergey.zhuravlev.mobile.social.data.mapper.transform.toException

@OptIn(ExperimentalPagingApi::class)
class ChatRemoteMediator(
  private val database: AppDatabase,
  private val networkService: ChatService
) : RemoteMediator<Int, ChatAndLastMessageEntities>() {

  private val chatModelDao = database.chatModelDao()
  private val messageModelDao = database.messageModelDao()

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, ChatAndLastMessageEntities>
  ): MediatorResult {

    return when (loadType) {
      LoadType.REFRESH -> refresh()
      LoadType.APPEND -> append()
      LoadType.PREPEND -> MediatorResult.Success(true)
    }
  }

  private suspend fun refresh(): MediatorResult {
    val pageNumber = 0

    val response = networkService.getCurrentUserChats(
      pageNumber,
      PAGE_SIZE,
      Sort.desc("lastMessage.createAt")
    )

    response.onSuccess { page ->
      database.withTransaction {
        // Reset all saved page instance after refreshing page:
        chatModelDao.resetPageableAfterPageMessageModel(pageNumber)
        // Update last messages in database:
        val updatedMessageEntities: List<MessageEntity> = updateMessageModels(page)
        // Create map where Key - Message.chatId, Value - Message
        val updatedChatLastMessageMap = updatedMessageEntities.associateBy { it.chatId }
        // Updating chat page in database with new network data:
        updateChatModels(page, updatedChatLastMessageMap)
      }
      return MediatorResult.Success(!page.hasNext)
    }

    response.onError { return MediatorResult.Error(it.toException()) }

    return MediatorResult.Error(
      IllegalStateException("Retrofit2 returned the response in an unfinished state")
    )
  }

  private suspend fun append(): MediatorResult {
    val lastPage = chatModelDao.getLastPage() ?: 0

    val response = networkService.getCurrentUserChats(
      lastPage + 1,
      PAGE_SIZE,
      Sort.desc("lastMessage.createAt")
    )

    response.onSuccess { page ->
      database.withTransaction {
        // Update last messages in database:
        val updatedMessageModels = updateMessageModels(page)
        // Create map where Key - Message.chatId, Value - Message
        val updatedChatLastMessageMap = updatedMessageModels.associateBy { it.chatId }
        // Updating chat page in database with new network data:
        updateChatModels(page, updatedChatLastMessageMap)
      }
    }

    response.onError { return MediatorResult.Error(it.toException()) }

    return MediatorResult.Error(
      IllegalStateException("Retrofit2 returned the response in an unfinished state")
    )
  }

  // Note: This method is transactional
  private suspend fun updateChatModels(
    page: PageDto<ChatPreviewDto>,
    updatedChatLastMessageMap: Map<Long, MessageEntity>
  ) {
    val content = page.content
    val newChatIds = content.map { it.id }
    val currentChatModels = chatModelDao.getAllChatAndLastMessageModelByIds(newChatIds)
      .associateBy { it.chat.id }
    val updatedChatEntities: MutableList<ChatEntity> = ArrayList()
    for (chatPreviewDto in content) {
      val model = if (chatPreviewDto.id in currentChatModels) {
        var newLastMessageModel = updatedChatLastMessageMap[chatPreviewDto.id]
        val currentLastMessageModel = currentChatModels[chatPreviewDto.id]!!.lastMessage
        if (currentLastMessageModel != newLastMessageModel
          && currentLastMessageModel.createAt.isAfter(newLastMessageModel!!.createAt)
        ) {
          newLastMessageModel = currentLastMessageModel
        }
        ChatEntityMapper.updateEntity(
          currentChatModels[chatPreviewDto.id]!!.chat,
          chatPreviewDto,
          newLastMessageModel
        )
      } else {
        ChatEntityMapper.toEntity(
          chatPreviewDto,
          updatedChatLastMessageMap[chatPreviewDto.id]!!
        )
      }
      model.pageable = Pageable(page.number)
      updatedChatEntities.add(model)
    }
    chatModelDao.insertAll(updatedChatEntities)
  }

  // Note: This method is transactional
  private suspend fun updateMessageModels(page: PageDto<ChatPreviewDto>): List<MessageEntity> {
    val networkIds = page.content.mapNotNull { it.lastMessage?.id }
    val currentModels = messageModelDao.getAllByNetworkIds(networkIds)
      .associateBy { it.networkId }
    val updatedModels: MutableList<MessageEntity> = ArrayList()
    for (chat in page.content) {
      chat.lastMessage?.let { lastMessage ->
        updatedModels.add(
          currentModels[lastMessage.id]?.let {
            MessageEntityMapper.updateEntity(it, lastMessage)
          } ?:
          MessageEntityMapper.toEntity(lastMessage, chat.id)
        )
      }
    }
    val ids = messageModelDao.insertAll(updatedModels)
    return updatedModels.mapIndexed { index, model ->
      model.id = ids[index]
      model
    }
  }

  companion object {
    private const val PAGE_SIZE = 20
  }
}

