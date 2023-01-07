package com.sergey.zhuravlev.mobile.social.data.mapper.transform

import android.util.Log
import com.sergey.zhuravlev.mobile.social.data.OperationErrorCodes
import com.sergey.zhuravlev.mobile.social.data.api.common.ErrorResponse
import com.sergey.zhuravlev.mobile.social.data.api.common.FieldErrorData
import com.sergey.zhuravlev.mobile.social.data.api.common.SocialResponse
import com.sergey.zhuravlev.mobile.social.domain.exception.OperationException
import com.sergey.zhuravlev.mobile.social.domain.model.ErrorModel
import com.sergey.zhuravlev.mobile.social.domain.model.Operation
import java.util.*

fun <T : ErrorResponse.ErrorEntry> ErrorResponse<T>.toErrorModel(): ErrorModel {
  return when (this) {
    is ErrorResponse.ApiError -> {
      Log.e(
        "Operation/apiError",
        "Operation api error = ${data?.code}/${data?.message ?: message}"
      )
      ErrorModel(
        messageCode = data?.code?.name?.screamingSnakeCaseToSnakeCase()
          ?: OperationErrorCodes.UNKNOWN_ERROR,
        fieldError = data?.fields?.map { it.toFieldErrorModel() } ?: emptyList()
      )
    }
    is ErrorResponse.NetworkError -> {
      Log.e(
        "Operation/networkError",
        "Operation network error: ",
        error
      )
      ErrorModel(messageCode = OperationErrorCodes.NETWORK_ERROR)
    }
  }
}

fun FieldErrorData.toFieldErrorModel(): ErrorModel.FieldErrorModel {
  return ErrorModel.FieldErrorModel(
    messageCode = this.code.name.screamingSnakeCaseToSnakeCase(),
    field = this.field
  )
}

fun <T : ErrorResponse.ErrorEntry> ErrorResponse<T>.toException(): OperationException {
  return when (this) {
    is ErrorResponse.ApiError ->
      OperationException(
        message = data?.message ?: "Unknown API error",
        errorData = toErrorModel()
      )
    is ErrorResponse.NetworkError ->
      OperationException(
        message = "Unknown network error",
        cause = error,
        errorData = toErrorModel()
      )
  }
}

fun <T> SocialResponse<T>.toOperation(): Operation<Void> {
  return if (this.success) {
    Operation.success()
  } else {
    this.errorData?.let {
      Operation.error(it.toErrorModel())
    } ?: throw IllegalStateException("impossible")
  }
}

private fun String.screamingSnakeCaseToSnakeCase(): String {
  return this.lowercase(Locale.getDefault())
}