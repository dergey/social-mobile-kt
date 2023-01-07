package com.sergey.zhuravlev.mobile.social.ui.login

data class LoginFormState(
  val emailError: String? = null,
  val passwordError: String? = null
) {
  val valid: Boolean
    get() = emailError == null && passwordError == null
}