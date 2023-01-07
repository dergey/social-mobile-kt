package com.sergey.zhuravlev.mobile.social.domain.model

import com.sergey.zhuravlev.mobile.social.domain.enums.MessageActionType

data class MessageActionModel(
  val type: MessageActionType,
  val messageModel: MessageModel?
)