package com.sergey.zhuravlev.mobile.social.data.api.common

import com.sergey.zhuravlev.mobile.social.data.api.enums.ErrorCode

data class ErrorData(
  override val code: ErrorCode,
  override val message: String,
  override val fields: List<FieldErrorData>
) : ErrorResponse.ErrorEntry