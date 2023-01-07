package com.sergey.zhuravlev.mobile.social.data.api.dto

data class AddressDto(
  val firstLine: String?,
  val secondLine: String?,
  val city: String?,
  val country: String,
  val zipCode: String?
)