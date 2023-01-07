package com.sergey.zhuravlev.mobile.social.app

import android.app.Application
import com.sergey.zhuravlev.mobile.social.di.appModule
import com.sergey.zhuravlev.mobile.social.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@App)
      modules(appModule, dataModule)
    }
  }

}