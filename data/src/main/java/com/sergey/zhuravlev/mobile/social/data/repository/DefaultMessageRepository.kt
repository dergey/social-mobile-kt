package com.sergey.zhuravlev.mobile.social.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.*
import androidx.room.withTransaction
import com.sergey.zhuravlev.mobile.social.data.api.dto.message.CreateStickerMessageDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.message.CreateTextMessageDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.message.UpdateTextMessageDto
import com.sergey.zhuravlev.mobile.social.data.api.service.MessageService
import com.sergey.zhuravlev.mobile.social.data.database.AppDatabase
import com.sergey.zhuravlev.mobile.social.data.database.dao.ChatModelDao
import com.sergey.zhuravlev.mobile.social.data.database.dao.MessageModelDao
import com.sergey.zhuravlev.mobile.social.data.database.entity.MessageEntity
import com.sergey.zhuravlev.mobile.social.data.datasource.GlideDataSource
import com.sergey.zhuravlev.mobile.social.data.mapper.MessageEntityMapper
import com.sergey.zhuravlev.mobile.social.data.mapper.transform.toDomainModel
import com.sergey.zhuravlev.mobile.social.data.mapper.transform.toErrorModel
import com.sergey.zhuravlev.mobile.social.data.mediator.MessageRemoteMediator
import com.sergey.zhuravlev.mobile.social.domain.model.MessageModel
import com.sergey.zhuravlev.mobile.social.domain.model.Operation
import com.sergey.zhuravlev.mobile.social.domain.repository.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.LocalDateTime

@OptIn(ExperimentalPagingApi::class)
class DefaultMessageRepository(
  private val database: AppDatabase,
  private val messageModelDao: MessageModelDao,
  private val chatModelDao: ChatModelDao,
  private val glideDataSource: GlideDataSource,
  private val messageService: MessageService
) : MessageRepository {

  override fun getChatMessages(chatId: Long): LiveData<PagingData<MessageModel>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20,
        enablePlaceholders = false
      ),
      remoteMediator = MessageRemoteMediator(chatId, database, messageService),
      pagingSourceFactory = {
        messageModelDao.getAllMessageModel(chatId)
      }
    ).liveData
      .map { pagingData ->
        pagingData.map { it.toDomainModel() }
      }
  }

  override suspend fun createTextMessage(chatId: Long, text: String): Operation<MessageModel> {
    return withContext(Dispatchers.IO) {
      // Insert perpend message:
      val messageEntity = MessageEntity(
        chatId = chatId,
        type = "TEXT",
        sender = "SOURCE",
        createAt = LocalDateTime.now(),
        updateAt = LocalDateTime.now(),
        read = false,
        text = text,
        prepend = true,
        hasPrependError = false,
      )
      database.withTransaction {
        val entityId = messageModelDao.insert(messageEntity)
        val chat = chatModelDao.getOneById(messageEntity.chatId)
          ?: throw IllegalStateException("Chat not found")
        messageEntity.id = entityId
        chat.lastMessageId = entityId
        chatModelDao.insert(chat)
      }

      // Attempting a remote call:
      val response = messageService.createMessage(chatId, CreateTextMessageDto(text = text))

      // Updating the prepend model:
      response.onSuccess {
        val updatedEntity = MessageEntityMapper.updateEntity(messageEntity, it)
        messageModelDao.insert(updatedEntity)
        return@withContext Operation.of(updatedEntity.toDomainModel())
      }

      // Updating the prepend model:
      response.onError {
        messageEntity.hasPrependError = true
        messageModelDao.insert(messageEntity)
      }

      return@withContext response.errorData?.let {
        Operation.error<MessageModel>(it.toErrorModel())
      } ?: throw IllegalStateException("impossible")
    }
  }

  override suspend fun createImageMessage(chatId: Long, filePath: String): Operation<MessageModel> {
    return withContext(Dispatchers.IO) {
      val fileUri = Uri.parse(filePath)

      // Insert perpend message:
      val messageEntity = MessageEntity(
        chatId = chatId,
        type = "IMAGE",
        sender = "SOURCE",
        createAt = LocalDateTime.now(),
        updateAt = LocalDateTime.now(),
        read = false,
        glideSignature = filePath,
        prepend = true,
        hasPrependError = false,
      )
      val compressedImage = glideDataSource.compressImage(fileUri, filePath)

      database.withTransaction {
        val entityId = messageModelDao.insert(messageEntity)
        val chat = chatModelDao.getOneById(messageEntity.chatId)
          ?: throw IllegalStateException("Chat not found")
        messageEntity.id = entityId
        chat.lastMessageId = entityId
        chatModelDao.insert(chat)
      }

      // Create request body:
      val body = MultipartBody.Part.createFormData(
        "image", compressedImage.filename,
        compressedImage.bytearray.toRequestBody("multipart/form-data".toMediaType())
      )

      // Attempting a remote call:
      val response = messageService.createImageMessage(chatId, body)

      // Updating the prepend model:
      response.onSuccess {
        val updatedEntity = MessageEntityMapper.updateEntity(messageEntity, it)
        messageModelDao.insert(updatedEntity)
        return@withContext Operation.of(updatedEntity.toDomainModel())
      }

      // Updating the prepend model:
      response.onError {
        messageEntity.hasPrependError = true
        messageModelDao.insert(messageEntity)
      }

      return@withContext response.errorData?.let {
        Operation.error<MessageModel>(it.toErrorModel())
      } ?: throw IllegalStateException("impossible")
    }
  }

  override suspend fun createStickerMessage(
    chatId: Long,
    stickerId: Long
  ): Operation<MessageModel> {
    return withContext(Dispatchers.IO) {
      val response = messageService.createMessage(chatId, CreateStickerMessageDto(stickerId = stickerId))

      response.onSuccess {
        val message = MessageEntityMapper.toEntity(it, chatId)
        messageModelDao.insert(message)
        return@withContext Operation.of(message.toDomainModel())
      }

      return@withContext response.errorData?.let {
        Operation.error<MessageModel>(it.toErrorModel())
      } ?: throw IllegalStateException("impossible")
    }
  }

  override suspend fun updateTextMessage(
    chatId: Long,
    messageId: Long,
    text: String
  ): Operation<Void> {
    return withContext(Dispatchers.IO) {
      val response = messageService.updateTextMessage(chatId, messageId, UpdateTextMessageDto(text = text))

      response.onSuccess {
        val message = MessageEntityMapper.toEntity(it, chatId)
        messageModelDao.insert(message)
        return@withContext Operation.success()
      }

      return@withContext response.errorData?.let {
        Operation.error<Void>(it.toErrorModel())
      } ?: throw IllegalStateException("impossible")
    }
  }

  override suspend fun deleteMessage(chatId: Long, messageId: Long): Operation<Void> {
    return withContext(Dispatchers.IO) {
      val response = messageService.deleteMessage(chatId, messageId)

      response.onSuccess {
        messageModelDao.clear(messageId)
        return@withContext Operation.success()
      }

      return@withContext response.errorData?.let {
        Operation.error<Void>(it.toErrorModel())
      } ?: throw IllegalStateException("impossible")
    }
  }
}