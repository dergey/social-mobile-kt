package com.sergey.zhuravlev.mobile.social.data.api.dto.event

import com.sergey.zhuravlev.mobile.social.data.api.dto.message.MessageDto

data class MessageCreatedEventDto(
  val chatId: Long,
  val message: MessageDto,
)