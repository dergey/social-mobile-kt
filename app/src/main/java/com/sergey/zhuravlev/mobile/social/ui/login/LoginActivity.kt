package com.sergey.zhuravlev.mobile.social.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.sergey.zhuravlev.mobile.social.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val vm: LoginViewModel by viewModel()

    val binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val emailEditText = binding.emailEditText
    val passwordEditText = binding.passwordEditText
    val loginButton = binding.loginButton
    val signUpTextView = binding.signUpTextView
    val errorLayout = binding.errorLayout
    val errorTextView = binding.errorTextView

    vm.loginFormState.observe(this) { state ->
      loginButton.isEnabled = state.valid
      errorLayout.visibility = View.INVISIBLE
      state.emailError?.let { emailEditText.error = it }
      state.passwordError?.let { passwordEditText.error = it }
    }

    vm.error.observe(this) { error ->
      errorLayout.visibility = View.VISIBLE
      errorTextView.text = error.messageCode
    }

    vm.isLogin.observe(this) { isLogin ->
      if (isLogin) {
        finish()
      }
    }

    val afterTextChangedListener: TextWatcher = object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

      override fun afterTextChanged(s: Editable) {
        vm.loginDataChanged(
          emailEditText.text.toString(),
          passwordEditText.text.toString()
        )
      }
    }

    emailEditText.addTextChangedListener(afterTextChangedListener)
    passwordEditText.addTextChangedListener(afterTextChangedListener)

    passwordEditText.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        vm.login(
          emailEditText.text.toString(),
          passwordEditText.text.toString()
        )
      }
      false
    }

    loginButton.setOnClickListener {
      vm.login(
        emailEditText.text.toString(),
        passwordEditText.text.toString()
      )
    }
  }

  companion object {
    fun open(context: Context) {
      val intent = Intent(context, LoginActivity::class.java)
      context.startActivity(intent)
    }
  }
}
