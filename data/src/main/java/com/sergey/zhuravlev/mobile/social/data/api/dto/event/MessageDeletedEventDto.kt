package com.sergey.zhuravlev.mobile.social.data.api.dto.event

data class MessageDeletedEventDto(
    val chatId: Long,
    val messageId: Long
)