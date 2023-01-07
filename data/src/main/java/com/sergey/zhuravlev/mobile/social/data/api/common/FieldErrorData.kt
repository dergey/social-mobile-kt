package com.sergey.zhuravlev.mobile.social.data.api.common

import com.sergey.zhuravlev.mobile.social.data.api.enums.ErrorCode

data class FieldErrorData(
  val field: String,
  val code: ErrorCode
)
