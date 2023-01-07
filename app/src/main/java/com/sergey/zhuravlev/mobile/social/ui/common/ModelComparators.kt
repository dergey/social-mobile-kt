package com.sergey.zhuravlev.mobile.social.ui.common

import androidx.recyclerview.widget.DiffUtil
import com.sergey.zhuravlev.mobile.social.domain.model.ChatModel
import com.sergey.zhuravlev.mobile.social.domain.model.ProfileModel

object ModelComparators {

  val CHAT_COMPARATOR = object : DiffUtil.ItemCallback<ChatModel>() {
    override fun areItemsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean =
      oldItem.networkId == newItem.networkId

    override fun areContentsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean =
      oldItem == newItem
  }

  val PROFILE_COMPARATOR = object : DiffUtil.ItemCallback<ProfileModel>() {
    override fun areItemsTheSame(oldItem: ProfileModel, newItem: ProfileModel): Boolean =
      oldItem.username == newItem.username

    override fun areContentsTheSame(oldItem: ProfileModel, newItem: ProfileModel): Boolean =
      oldItem == newItem
  }

}