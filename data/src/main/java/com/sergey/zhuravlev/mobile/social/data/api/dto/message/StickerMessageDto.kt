package com.sergey.zhuravlev.mobile.social.data.api.dto.message

import com.fasterxml.jackson.annotation.JsonProperty
import com.sergey.zhuravlev.mobile.social.data.api.enums.MessageSenderType
import com.sergey.zhuravlev.mobile.social.data.api.enums.MessageType
import java.time.LocalDateTime

data class StickerMessageDto(
  override val id: Long,
  override val type: MessageType = MessageType.STICKER,
  override val sender: MessageSenderType,
  override val createAt: LocalDateTime,
  override val updateAt: LocalDateTime,
  @JsonProperty("isRead")
  override val read: Boolean
) : MessageDto()