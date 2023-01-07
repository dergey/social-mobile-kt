package com.sergey.zhuravlev.mobile.social.ui.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sergey.zhuravlev.mobile.social.R
import com.sergey.zhuravlev.mobile.social.domain.model.ChatModel
import com.sergey.zhuravlev.mobile.social.ui.common.DataFormatters.toTimeFormattedString
import com.sergey.zhuravlev.mobile.social.ui.message.MessageListActivity


class ChatViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
  LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
), View.OnClickListener {

  private val profileAvatarImageView: ImageView =
    itemView.findViewById(R.id.profile_avatar_image_view)
  private val unreadMessagesTextView: TextView =
    itemView.findViewById(R.id.unread_messages_text_view)
  private val chatTitleTextView: TextView =
    itemView.findViewById(R.id.chat_title_text_view)
  private val lastMessageCreateAtTextView: TextView =
    itemView.findViewById(R.id.last_message_create_at_text_view)
  private val lastMessageTextTextView: TextView =
    itemView.findViewById(R.id.last_message_text_text_view)
  private val lastMessageReadImageView: ImageView =
    itemView.findViewById(R.id.last_message_read_image_view)

  private lateinit var context: Context
  private lateinit var chat: ChatModel

  init {
    itemView.setOnClickListener(this)
  }

  fun bindTo(item: ChatModel, context: Context) {
    this.context = context
    this.chat = item

    chatTitleTextView.text = listOfNotNull(
      chat.targetProfile.firstName,
      chat.targetProfile.secondName
    )
      .joinToString(separator = " ")

    lastMessageTextTextView.text = chat.lastMessage?.let {
      when (it.type) {
        "TEXT", "SERVICE" -> it.text
        "IMAGE" -> context.getString(R.string.message_image_text)
        "STICKER" -> context.getString(R.string.message_sticker_text)
        else -> null
      }
    }

    lastMessageCreateAtTextView.text = chat.lastMessage?.createAt?.toTimeFormattedString()

    unreadMessagesTextView.text = chat.unreadMessages.toString()
    unreadMessagesTextView.visibility = if (chat.unreadMessages > 0)
      View.VISIBLE
    else
      View.GONE

    if (chat.lastMessage?.sender == "SOURCE") {
      if (chat.lastMessage!!.read) {
        lastMessageReadImageView.setImageResource(R.drawable.ic_round_done_all_24)
      } else {
        lastMessageReadImageView.setImageResource(R.drawable.ic_round_done_24)
      }
      lastMessageReadImageView.visibility = View.VISIBLE
    } else {
      lastMessageReadImageView.visibility = View.GONE
    }

    Glide.with(context)
      .load(chat.targetProfile.avatarLink)
      .circleCrop()
      .into(profileAvatarImageView)
  }

  override fun onClick(v: View?) {
    MessageListActivity.open(context, chat)
  }
}