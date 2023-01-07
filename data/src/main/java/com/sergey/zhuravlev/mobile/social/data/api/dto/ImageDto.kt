package com.sergey.zhuravlev.mobile.social.data.api.dto

import java.time.LocalDateTime

data class ImageDto(
  val mimeType: String,
  val height: Int,
  val width: Int,
  val dataSize: String,
  val createAt: LocalDateTime,
)