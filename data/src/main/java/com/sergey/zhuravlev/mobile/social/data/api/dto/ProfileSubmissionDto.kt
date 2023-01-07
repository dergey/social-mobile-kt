package com.sergey.zhuravlev.mobile.social.data.api.dto

import com.sergey.zhuravlev.mobile.social.data.api.enums.Gender

data class ProfileSubmissionDto(
  val firstName: String?,
  val middleName: String,
  val secondName: String?,
  val gender: Gender?,
)