package com.sergey.zhuravlev.mobile.social.data.api.dto.sticker

import java.time.LocalDateTime

data class StickerDto(
  val mimeType: String,
  val emoji: String,
  val height: Int,
  val width: Int,
  val dataSize: String,
  val createAt: LocalDateTime
)