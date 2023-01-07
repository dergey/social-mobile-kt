package com.sergey.zhuravlev.mobile.social.ui.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergey.zhuravlev.mobile.social.R
import com.sergey.zhuravlev.mobile.social.data.datasource.BarrierTokenDataSource
import com.sergey.zhuravlev.mobile.social.domain.model.ErrorModel
import com.sergey.zhuravlev.mobile.social.domain.model.Operation
import com.sergey.zhuravlev.mobile.social.domain.repository.AuthenticationRepository
import kotlinx.coroutines.launch

class LoginViewModel(
  private val authenticationRepository: AuthenticationRepository,
  private val tokenDataSource: BarrierTokenDataSource
) : ViewModel() {

  private val _error = MutableLiveData<ErrorModel>()

  val error: LiveData<ErrorModel> = _error

  private val _loginFormState = MutableLiveData<LoginFormState>()

  val loginFormState: LiveData<LoginFormState> = _loginFormState

  private val _isLogin = MutableLiveData<Boolean>().apply {
    value = tokenDataSource.isLogin()
  }

  val isLogin: LiveData<Boolean> = _isLogin

  fun login(email: String, password: String) {
    viewModelScope.launch {
      val operation = authenticationRepository.authenticate(email, password)
      operation
        .takeIf { it.success }
        .takeIf { tokenDataSource.isLogin() }
        ?.let { _isLogin.postValue(true) }
      operation
        .takeIf { !it.success }
        ?.let {
          _error.postValue(it.errorData!!)
          _loginFormState.postError(it.errorData!!)
        }
    }
  }

  private fun MutableLiveData<LoginFormState>.postError(error: ErrorModel) {
    if (error.messageCode == "not_valid" || error.messageCode != "field_error") {
      val fieldError = error.fieldError.associateBy { it.field }
      postValue(LoginFormState(
        emailError = fieldError["email"]?.messageCode,
        passwordError = fieldError["password"]?.messageCode
      ))
    }
  }

  fun loginDataChanged(username: String?, password: String?) {
    if (!isEmailValid(username)) {
      _loginFormState.value = LoginFormState(emailError = "Invalid email")
    } else if (!isPasswordValid(password)) {
      _loginFormState.value = LoginFormState(passwordError = "Invalid password")
    } else {
      _loginFormState.setValue(LoginFormState())
    }
  }

  private fun isEmailValid(username: String?): Boolean {
    if (username == null) {
      return false
    }
    return if (username.contains("@")) {
      Patterns.EMAIL_ADDRESS.matcher(username).matches()
    } else {
      !username.trim { it <= ' ' }.isEmpty()
    }
  }

  private fun isPasswordValid(password: String?): Boolean {
    return password != null && password.trim { it <= ' ' }.length > 5
  }
}


