package com.sergey.zhuravlev.mobile.social.domain.model

data class Operation<T>(
  val data: T? = null,
  val errorData: ErrorModel? = null,
  val success: Boolean
) {

  companion object {
    fun <T> of(result: T): Operation<T> {
      return Operation(
        data = result,
        success = true
      )
    }

    fun success(): Operation<Void> {
      return Operation(
        success = true
      )
    }

    fun <T> error(error: ErrorModel): Operation<T> {
      return Operation(
        errorData = error,
        success = false
      )
    }
  }
}