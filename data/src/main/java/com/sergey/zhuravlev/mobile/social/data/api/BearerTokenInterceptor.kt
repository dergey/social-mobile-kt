package com.sergey.zhuravlev.mobile.social.data.api

import okhttp3.Interceptor
import okhttp3.Response

internal class BearerTokenInterceptor : Interceptor {

  var barrierToken: String? = null

  override fun intercept(chain: Interceptor.Chain): Response {
    val requestBuilder = chain
      .request()
      .newBuilder()
    if (barrierToken != null) {
      requestBuilder.addHeader("Authorization", "Bearer $barrierToken")
    }
    return chain.proceed(requestBuilder.build())
  }
}