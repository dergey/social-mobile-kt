package com.sergey.zhuravlev.mobile.social.data.api.dto

data class PageDto<T>(
  val size: Int,
  val number: Int,
  val totalPages: Int,
  val totalElements: Int,
  val hasNext: Boolean,
  val content: List<T>
)