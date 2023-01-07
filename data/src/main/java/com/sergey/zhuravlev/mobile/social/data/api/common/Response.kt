package com.sergey.zhuravlev.mobile.social.data.api.common

import android.util.Log

open class Response<S, E: ErrorResponse.ErrorEntry> {

  var success: Boolean = true

  var successData: S? = null

  var errorData: ErrorResponse<E>? = null

  inline fun onSuccess(block: (S) -> Unit) {
    if (success) {
      successData?.let(block)
        ?: Log.w("Response", "Warning! You are using onSuccess for a non-body operation")
    }
  }

  inline fun onError(block: (ErrorResponse<E>) -> Unit) {
    if (!success) {
      block(errorData!!)
    }
  }
}