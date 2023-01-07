package com.sergey.zhuravlev.mobile.social.data.api.dto

import com.sergey.zhuravlev.mobile.social.data.api.enums.AddressType

data class UpdateAddressDto(
  val type: AddressType,
  val firstLine: String?,
  val secondLine: String?,
  val city: String,
  val country: String,
  val zipCode: String,
)