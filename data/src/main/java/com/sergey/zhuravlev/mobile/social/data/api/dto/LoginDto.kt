package com.sergey.zhuravlev.mobile.social.data.api.dto

data class LoginDto(
  val email: String,
  val password: String,
  val rememberMe: Boolean = false
)