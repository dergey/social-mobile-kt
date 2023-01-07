package com.sergey.zhuravlev.mobile.social.data.api.dto.message

import com.sergey.zhuravlev.mobile.social.data.api.enums.MessageType

data class CreateStickerMessageDto(
  override val type: MessageType = MessageType.STICKER,
  val stickerId: Long
) : CreateMessageDto()