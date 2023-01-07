package com.sergey.zhuravlev.mobile.social.domain.exception

import com.sergey.zhuravlev.mobile.social.domain.model.ErrorModel

class OperationException : RuntimeException {

  val errorData: ErrorModel

  constructor(message: String, errorData: ErrorModel) : super(message) {
    this.errorData = errorData
  }

  constructor(message: String, cause: Throwable, errorData: ErrorModel) : super(message, cause) {
    this.errorData = errorData
  }
}