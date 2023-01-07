package com.sergey.zhuravlev.mobile.social.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
class ChatModel(
  val networkId: Long,
  val targetProfile: ProfileModel,
  val createAt: LocalDateTime,
  val updateAt: LocalDateTime,
  val messageAllow: Boolean = true,
  val unreadMessages: Long = 0,
  val lastMessage: MessageModel? = null
) : Parcelable {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ChatModel

    if (networkId != other.networkId) return false
    if (targetProfile != other.targetProfile) return false
    if (createAt != other.createAt) return false
    if (updateAt != other.updateAt) return false
    if (messageAllow != other.messageAllow) return false
    if (unreadMessages != other.unreadMessages) return false
    if (lastMessage != other.lastMessage) return false

    return true
  }
  override fun hashCode(): Int {
    var result = networkId.hashCode()
    result = 31 * result + targetProfile.hashCode()
    result = 31 * result + createAt.hashCode()
    result = 31 * result + updateAt.hashCode()
    result = 31 * result + messageAllow.hashCode()
    result = 31 * result + (unreadMessages.hashCode())
    result = 31 * result + (lastMessage?.hashCode() ?: 0)
    return result
  }
}