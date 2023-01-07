package com.sergey.zhuravlev.mobile.social.ui.message

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.sergey.zhuravlev.mobile.social.domain.model.ChatModel
import com.sergey.zhuravlev.mobile.social.domain.model.ErrorModel
import com.sergey.zhuravlev.mobile.social.domain.repository.ChatRepository
import com.sergey.zhuravlev.mobile.social.domain.repository.MessageAsyncRepository
import com.sergey.zhuravlev.mobile.social.domain.repository.MessageRepository
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class MessageListViewModel(
  private val chatRepository: ChatRepository,
  private val messageRepository: MessageRepository,
  private val messageAsyncRepository: MessageAsyncRepository
) : ViewModel() {

  private var _chat by Delegates.notNull<ChatModel>()

  val chat: ChatModel
    get() = _chat

  private val _error = MutableLiveData<ErrorModel>()

  val error: LiveData<ErrorModel> = _error

  val messages: LiveData<PagingData<MessageItem>> by lazy {
    messageRepository.getChatMessages(_chat.networkId)
      .map { pagingData ->
        pagingData
          .map {
            MessageItem.RepoItem(it)
          }
          .insertSeparators { before, after ->
            val beforeCreateAt = before?.model?.createAt?.toLocalDate()
            val afterCreateAt = after?.model?.createAt?.toLocalDate()

            if (beforeCreateAt != null && afterCreateAt == null) {
              MessageItem.DateSeparatorItem(beforeCreateAt)
            } else if (beforeCreateAt != null && afterCreateAt != beforeCreateAt) {
              MessageItem.DateSeparatorItem(beforeCreateAt)
            } else {
              null
            }
          }
      }
      .cachedIn(viewModelScope)
  }

  fun createTextMessage(text: String) {
    viewModelScope.launch {
      messageRepository.createTextMessage(chat.networkId, text)
        .takeIf { !it.success }
        ?.let {
          _error.postValue(it.errorData!!)
        }
    }
  }

  fun createImageMessage(uri: Uri) {
    viewModelScope.launch {
      messageRepository.createImageMessage(chat.networkId, uri.toString())
        .takeIf { !it.success }
        ?.let {
          _error.postValue(it.errorData!!)
        }
    }
  }

  fun deleteMessage(messageNetworkId: Long) {
    viewModelScope.launch {
      messageRepository.deleteMessage(chat.networkId, messageNetworkId)
        .takeIf { !it.success }
        ?.let {
          _error.postValue(it.errorData!!)
        }
    }
  }

  fun updateReadStatus() {
    viewModelScope.launch {
      chatRepository.updateReadStatus(chat.networkId)
        .takeIf { !it.success }
        ?.let {
          _error.postValue(it.errorData!!)
        }
    }
  }

  fun subscribeMessages() {
//    viewModelScope.launch {
//      messageAsyncRepository.subscribe()
//    }
  }

  fun parseIntent(intent: Intent) {
    val chat: ChatModel = intent.getParcelableExtra(MessageListActivity.INTENT_ARG_CHAT)
      ?: throw IllegalStateException("MessageActivity start without required args")
    this._chat = chat
  }
}