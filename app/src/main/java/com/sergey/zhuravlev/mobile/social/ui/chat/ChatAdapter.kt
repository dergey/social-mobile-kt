package com.sergey.zhuravlev.mobile.social.ui.chat

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.sergey.zhuravlev.mobile.social.domain.model.ChatModel
import com.sergey.zhuravlev.mobile.social.ui.common.ModelComparators

class ChatAdapter(
  private val context: Context
) : PagingDataAdapter<ChatModel, ChatViewHolder>(ModelComparators.CHAT_COMPARATOR) {

  override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
    getItem(position)?.let { holder.bindTo(it, context) }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
    return ChatViewHolder(parent)
  }
}