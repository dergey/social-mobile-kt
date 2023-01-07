package com.sergey.zhuravlev.mobile.social.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sergey.zhuravlev.mobile.social.domain.model.ChatModel
import com.sergey.zhuravlev.mobile.social.domain.model.ErrorModel
import com.sergey.zhuravlev.mobile.social.domain.repository.ChatRepository

class ChatListViewModel(
  chatRepository: ChatRepository
) : ViewModel() {

  private val _error = MutableLiveData<ErrorModel>()
  val error: LiveData<ErrorModel> = _error

  val chats: LiveData<PagingData<ChatModel>> =
    chatRepository.getCurrentUserChats().cachedIn(viewModelScope)

}