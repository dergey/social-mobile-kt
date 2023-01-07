package com.sergey.zhuravlev.mobile.social.data.api.dto.event

import com.sergey.zhuravlev.mobile.social.data.api.dto.message.MessageDto

data class MessageUpdatedEventDto(
  val chatId: Long,
  val message: MessageDto,
)