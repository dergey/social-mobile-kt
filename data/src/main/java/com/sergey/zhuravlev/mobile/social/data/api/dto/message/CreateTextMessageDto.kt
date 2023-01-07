package com.sergey.zhuravlev.mobile.social.data.api.dto.message

import com.sergey.zhuravlev.mobile.social.data.api.enums.MessageType

data class CreateTextMessageDto(
  override val type: MessageType = MessageType.TEXT,
  val text: String
) : CreateMessageDto()