package com.sergey.zhuravlev.mobile.social.data.api.dto

import com.sergey.zhuravlev.mobile.social.data.api.dto.message.MessageDto
import java.time.LocalDateTime

data class ChatDto(
  val id: Long,
  val targetProfile: ProfilePreviewDto,
  val createAt: LocalDateTime,
  val updateAt: LocalDateTime,
  val messageAllow: Boolean,
  val lastMessages: List<MessageDto>
)