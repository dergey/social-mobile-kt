package com.sergey.zhuravlev.mobile.social.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class MessageModel(
  val id: Long?,
  val networkId: Long?,
  val chatNetworkId: Long,
  val type: String,
  val sender: String,
  val createAt: LocalDateTime,
  val updateAt: LocalDateTime,
  val read: Boolean,
  val text: String? = null,
  val imageLink: String? = null,
  val glideSignature: String? = null,
  val prepend: Boolean = false,
  val hasPrependError: Boolean = false,
) : Parcelable {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as MessageModel

    if (id != other.id) return false
    if (networkId != other.networkId) return false
    if (chatNetworkId != other.chatNetworkId) return false
    if (type != other.type) return false
    if (sender != other.sender) return false
    if (createAt != other.createAt) return false
    if (updateAt != other.updateAt) return false
    if (read != other.read) return false
    if (text != other.text) return false
    if (glideSignature != other.glideSignature) return false
    if (prepend != other.prepend) return false
    if (hasPrependError != other.hasPrependError) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id?.hashCode() ?: 0
    result = 31 * result + (networkId?.hashCode() ?: 0)
    result = 31 * result + chatNetworkId.hashCode()
    result = 31 * result + type.hashCode()
    result = 31 * result + sender.hashCode()
    result = 31 * result + createAt.hashCode()
    result = 31 * result + updateAt.hashCode()
    result = 31 * result + read.hashCode()
    result = 31 * result + (text?.hashCode() ?: 0)
    result = 31 * result + (glideSignature?.hashCode() ?: 0)
    result = 31 * result + prepend.hashCode()
    result = 31 * result + hasPrependError.hashCode()
    return result
  }
}
