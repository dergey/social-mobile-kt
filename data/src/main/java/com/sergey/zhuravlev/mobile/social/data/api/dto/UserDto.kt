package com.sergey.zhuravlev.mobile.social.data.api.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class UserDto(
  val email: String,
  val username: String,
  val avatar: ImageDto?,
  val images: List<String>,
  val firstName: String,
  val middleName: String?,
  val secondName: String,
  val birthDate: LocalDate?,
  val createAt: LocalDateTime,
  val updateAt: LocalDateTime,
)