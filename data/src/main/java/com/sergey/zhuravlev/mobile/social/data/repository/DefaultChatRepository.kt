package com.sergey.zhuravlev.mobile.social.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.*
import androidx.room.withTransaction
import com.sergey.zhuravlev.mobile.social.data.api.dto.CreateChatDto
import com.sergey.zhuravlev.mobile.social.data.api.service.ChatService
import com.sergey.zhuravlev.mobile.social.data.database.AppDatabase
import com.sergey.zhuravlev.mobile.social.data.database.dao.ChatModelDao
import com.sergey.zhuravlev.mobile.social.data.database.dao.MessageModelDao
import com.sergey.zhuravlev.mobile.social.data.mapper.ChatEntityMapper
import com.sergey.zhuravlev.mobile.social.data.mapper.transform.toDomainModel
import com.sergey.zhuravlev.mobile.social.data.mapper.transform.toErrorModel
import com.sergey.zhuravlev.mobile.social.data.mapper.transform.toOperation
import com.sergey.zhuravlev.mobile.social.data.mediator.ChatRemoteMediator
import com.sergey.zhuravlev.mobile.social.domain.model.ChatModel
import com.sergey.zhuravlev.mobile.social.domain.model.Operation
import com.sergey.zhuravlev.mobile.social.domain.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class DefaultChatRepository(
  private val database: AppDatabase,
  private val chatModelDao: ChatModelDao,
  private val messageModelDao: MessageModelDao,
  private val chatService: ChatService
) : ChatRepository {

  override fun getCurrentUserChats(): LiveData<PagingData<ChatModel>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20,
        enablePlaceholders = false
      ),
      remoteMediator = ChatRemoteMediator(database, chatService),
      pagingSourceFactory = {
        chatModelDao.getAllChatAndLastMessageModel()
      }
    ).liveData
      .map { pagingData ->
        pagingData.map { it.toDomainModel() }
      }
  }

  override suspend fun getOrCreateChat(username: String): Operation<ChatModel> {
    return withContext(Dispatchers.IO) {
      val response = chatService.getOrCreateChat(CreateChatDto(targetUsername = username))

      response.onSuccess {
        val chat = ChatEntityMapper.toEntity(it)
        chatModelDao.insert(chat)
        return@withContext Operation.of(chat.toDomainModel())
      }

      return@withContext response.errorData?.let {
        Operation.error<ChatModel>(it.toErrorModel())
      } ?: throw IllegalStateException("impossible")
    }
  }

  override suspend fun updateReadStatus(chatId: Long): Operation<Void> {
    return withContext(Dispatchers.IO) {
      val response = chatService.updateReadStatus(chatId)

      response.onSuccess {
        database.withTransaction {
          // Update chat unread messages count:
          chatModelDao.getOneById(chatId)?.let {
            it.unreadMessages = 0L
            chatModelDao.insert(it)
          }
          // Update messages read flag:
          messageModelDao.getAllByChatIdAndMessageSenderType(chatId, "TARGET")
            .map {
              it.read = true
              it
            }
            .let {
              messageModelDao.insertAll(it)
            }
        }
      }

      return@withContext response.toOperation()
    }
  }

  override suspend fun blockChat(chatId: Long): Operation<Void> {
    return withContext(Dispatchers.IO) {
      val response = chatService.blockChat(chatId)

      response.onSuccess {
        val chat = ChatEntityMapper.toEntity(it)
        chatModelDao.insert(chat)
      }

      return@withContext response.toOperation()
    }
  }

  override suspend fun unblockChat(chatId: Long): Operation<Void> {
    return withContext(Dispatchers.IO) {
      val response = chatService.unblockChat(chatId)

      response.onSuccess {
        val chat = ChatEntityMapper.toEntity(it)
        chatModelDao.insert(chat)
      }

      return@withContext response.toOperation()
    }
  }
}