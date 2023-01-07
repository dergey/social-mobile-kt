package com.sergey.zhuravlev.mobile.social.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.sergey.zhuravlev.mobile.social.domain.model.ChatModel
import com.sergey.zhuravlev.mobile.social.domain.model.Operation
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

  fun getCurrentUserChats(): LiveData<PagingData<ChatModel>>

  suspend fun getOrCreateChat(username: String): Operation<ChatModel>

  suspend fun updateReadStatus(chatId: Long): Operation<Void>

  suspend fun blockChat(chatId: Long): Operation<Void>

  suspend fun unblockChat(chatId: Long): Operation<Void>
}