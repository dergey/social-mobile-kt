package com.sergey.zhuravlev.mobile.social.data.api.dto

import com.sergey.zhuravlev.mobile.social.data.api.enums.ProfileAttitude
import java.time.LocalDate

data class ProfilePreviewDto(
  val username: String,
  val firstName: String,
  val middleName: String?,
  val secondName: String,
  val city: String?,
  val birthDate: LocalDate?,
  val attitude: ProfileAttitude = ProfileAttitude.NONE,
)