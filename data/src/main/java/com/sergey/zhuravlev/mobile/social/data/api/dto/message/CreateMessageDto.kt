package com.sergey.zhuravlev.mobile.social.data.api.dto.message

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.sergey.zhuravlev.mobile.social.data.api.enums.MessageType

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type"
)
@JsonSubTypes(
  JsonSubTypes.Type(CreateTextMessageDto::class, name = "TEXT"),
  JsonSubTypes.Type(CreateStickerMessageDto::class, name = "STICKER")
)
abstract class CreateMessageDto {
  abstract val type: MessageType
}