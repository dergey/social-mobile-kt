package com.sergey.zhuravlev.mobile.social.data.api.common

import com.sergey.zhuravlev.mobile.social.data.api.enums.ErrorCode

sealed class ErrorResponse<T : ErrorResponse.ErrorEntry>(val message: String) {

  data class ApiError<T : ErrorEntry>(val data: T?, val code: Int) :
    ErrorResponse<T>(data?.message ?: "Unknown backend error. Status: $code")

  data class NetworkError<T : ErrorEntry>(val error: Throwable) :
    ErrorResponse<T>("Network error. ${error.message}")

  interface ErrorEntry {
    val code: ErrorCode
    val message: String
    val fields: List<FieldErrorData>
  }
}