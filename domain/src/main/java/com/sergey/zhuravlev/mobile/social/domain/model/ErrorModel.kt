package com.sergey.zhuravlev.mobile.social.domain.model

data class ErrorModel(
  val messageCode: String,
  val fieldError: List<FieldErrorModel> = emptyList()
) {

  data class FieldErrorModel(
    val messageCode: String,
    val field: String
  )
}
