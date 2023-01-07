package com.sergey.zhuravlev.mobile.social.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.sergey.zhuravlev.mobile.social.data.api.common.Sort
import com.sergey.zhuravlev.mobile.social.data.api.dto.PageDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.message.MessageDto
import com.sergey.zhuravlev.mobile.social.data.api.service.MessageService
import com.sergey.zhuravlev.mobile.social.data.database.AppDatabase
import com.sergey.zhuravlev.mobile.social.data.database.entity.MessageEntity
import com.sergey.zhuravlev.mobile.social.data.database.pageable.Pageable
import com.sergey.zhuravlev.mobile.social.data.mapper.MessageEntityMapper
import com.sergey.zhuravlev.mobile.social.data.mapper.transform.toException

@OptIn(ExperimentalPagingApi::class)
class MessageRemoteMediator(
  private val chatId: Long,
  private val database: AppDatabase,
  private val networkService: MessageService
) : RemoteMediator<Int, MessageEntity>() {

  private val messageModelDao = database.messageModelDao()

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, MessageEntity>
  ): MediatorResult {

    return when (loadType) {
      LoadType.REFRESH -> refresh(state)
      LoadType.APPEND -> append()
      LoadType.PREPEND -> MediatorResult.Success(true)
    }
  }

  private suspend fun refresh(state: PagingState<Int, MessageEntity>): MediatorResult {
    val currentPage: Int = getClosestRemoteKey(state)
    val response = networkService.getChatMessages(
      chatId, currentPage, PAGE_SIZE, Sort.desc("createAt")
    )

    response.onSuccess { page ->
      database.withTransaction {
        // Reset all saved page instance after refreshing page:
        messageModelDao.resetPageableAfterPageMessageModel(chatId, currentPage)
        // Updating page cache with new network data:
        val newNetworkIds: List<Long> = page.content.map { it.id }
        val currentMessageModels = messageModelDao.getAllByNetworkIds(newNetworkIds)
          .filter { it.networkId != null }
          .associateBy { it.networkId!! }
        val updatedPageableModels: List<MessageEntity> =
          updatePageableModels(currentMessageModels, page, chatId)
        messageModelDao.insertAll(updatedPageableModels)
      }
      return MediatorResult.Success(!page.hasNext)
    }
    response.onError { return MediatorResult.Error(it.toException()) }
    return MediatorResult.Error(
      IllegalStateException("Retrofit2 returned the response in an unfinished state")
    )
  }

  private suspend fun append(): MediatorResult {
    val lastPage = messageModelDao.getLastPage(chatId)
    val response = networkService.getChatMessages(chatId, lastPage + 1, PAGE_SIZE, Sort.desc("createAt"))
    response.onSuccess { page ->
      database.withTransaction {
        val newNetworkIds: List<Long> = page.content.map { it.id }
        val currentMessageModels = messageModelDao.getAllByNetworkIds(newNetworkIds)
          .filter { it.networkId != null }
          .associateBy { it.networkId!! }
        val updatedPageableModels: List<MessageEntity> =
          updatePageableModels(currentMessageModels, page, chatId)
        messageModelDao.insertAll(updatedPageableModels)
      }
      return MediatorResult.Success(!page.hasNext)
    }
    response.onError { return MediatorResult.Error(it.toException()) }
    return MediatorResult.Error(
      IllegalStateException("Retrofit2 returned the response in an unfinished state")
    )
  }

  private fun getClosestRemoteKey(state: PagingState<Int, MessageEntity>): Int {
    return state.anchorPosition?.let { position ->
      state.closestItemToPosition(position)?.let { item ->
        item.pageable?.page
      }
    } ?: PAGE_START
  }

  private fun updatePageableModels(
    oldModels: Map<Long, MessageEntity>,
    page: PageDto<MessageDto>,
    chatId: Long
  ): List<MessageEntity> {
    val models: MutableList<MessageEntity> = ArrayList()
    for (dto in page.content) {
      val model = if (dto.id in oldModels) {
        MessageEntityMapper.updateEntity(oldModels[dto.id]!!, dto)
      } else {
        MessageEntityMapper.toEntity(dto, chatId)
      }
      model.pageable = Pageable(page.number)
      models.add(model)
    }
    return models
  }

  companion object {
    private const val PAGE_START = 0
    private const val PAGE_SIZE = 20
  }
}

