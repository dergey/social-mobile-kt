package com.sergey.zhuravlev.mobile.social

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sergey.zhuravlev.mobile.social.databinding.ActivityMainBinding
import com.sergey.zhuravlev.mobile.social.ui.home.HomeActivity
import com.sergey.zhuravlev.mobile.social.ui.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val vm: MainViewModel by viewModel()

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    supportActionBar?.hide()

    vm.isLogin.observe(this) { isLogin ->
      if (isLogin) {
        HomeActivity.open(this)
      } else {
        LoginActivity.open(this)
      }
    }
  }
}