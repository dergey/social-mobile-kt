package com.sergey.zhuravlev.mobile.social.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.sergey.zhuravlev.mobile.social.domain.model.MessageModel
import com.sergey.zhuravlev.mobile.social.domain.model.Operation
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

  fun getChatMessages(chatId: Long): LiveData<PagingData<MessageModel>>

  suspend fun createTextMessage(chatId: Long, text: String): Operation<MessageModel>

  suspend fun createImageMessage(chatId: Long, filePath: String): Operation<MessageModel>

  suspend fun createStickerMessage(chatId: Long, stickerId: Long): Operation<MessageModel>

  suspend fun updateTextMessage(chatId: Long, messageId: Long, text: String): Operation<Void>

  suspend fun deleteMessage(chatId: Long, messageId: Long): Operation<Void>
}