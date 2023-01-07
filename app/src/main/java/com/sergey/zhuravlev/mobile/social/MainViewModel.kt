package com.sergey.zhuravlev.mobile.social

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sergey.zhuravlev.mobile.social.data.datasource.BarrierTokenDataSource

class MainViewModel(
  private val tokenDataSource: BarrierTokenDataSource
) : ViewModel() {

  private val _isLogin = MutableLiveData<Boolean>().apply {
    value = tokenDataSource.getBarrierToken() != null
  }

  val isLogin: LiveData<Boolean> = _isLogin

}