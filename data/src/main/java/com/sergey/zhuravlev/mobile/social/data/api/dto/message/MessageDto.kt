package com.sergey.zhuravlev.mobile.social.data.api.dto.message

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.sergey.zhuravlev.mobile.social.data.api.enums.MessageSenderType
import com.sergey.zhuravlev.mobile.social.data.api.enums.MessageType
import java.time.LocalDateTime

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type"
)
@JsonSubTypes(
  JsonSubTypes.Type(TextMessageDto::class, name = "TEXT"),
  JsonSubTypes.Type(ImageMessageDto::class, name = "IMAGE"),
  JsonSubTypes.Type(StickerMessageDto::class, name = "STICKER")
)
abstract class MessageDto {
  abstract val id: Long
  abstract val type: MessageType
  abstract val sender: MessageSenderType
  abstract val createAt: LocalDateTime
  abstract val updateAt: LocalDateTime
  abstract val read: Boolean
}