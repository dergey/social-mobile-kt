package com.sergey.zhuravlev.mobile.social.data.datasource

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.sergey.zhuravlev.mobile.social.data.api.SocialApi
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class BarrierTokenDataSource(private val context: Context) {

  init {
    context
      .getSharedPreferences(EXTRA_TOKEN, Context.MODE_PRIVATE)
      .getString(EXTRA_TOKEN, null)
      ?.let { SocialApi.setBearerToken(it) }
  }

  fun getBarrierToken(): String? {
    val settings: SharedPreferences =
      context.getSharedPreferences(EXTRA_TOKEN, Context.MODE_PRIVATE)
    val jwtToken = settings.getString(EXTRA_TOKEN, null)

    return jwtToken?.takeIf { !isTokenExpired(it) }
  }

  fun saveBarrierToken(token: String) {
    val settings: SharedPreferences = context.getSharedPreferences(
      EXTRA_TOKEN,
      Context.MODE_PRIVATE
    )
    val editor = settings.edit()
    editor.putString(EXTRA_TOKEN, token)
    editor.apply()
  }

  fun invalidateBarrierToken() {
    val settings: SharedPreferences = context.getSharedPreferences(
      EXTRA_TOKEN,
      Context.MODE_PRIVATE
    )
    val editor = settings.edit()
    editor.remove(EXTRA_TOKEN)
    editor.apply()
  }

  private fun isTokenExpired(token: String): Boolean {
    val unsignedToken = token.substringBeforeLast('.') + "."
    val claims: Claims = Jwts.parserBuilder().build()
      .parse(unsignedToken)
      .body as Claims

    val expiration = LocalDateTime.ofInstant(
      Instant.ofEpochMilli((claims[Claims.EXPIRATION] as Int) * 1000L),
      ZoneId.of("UTC")
    )

    return expiration.isBefore(LocalDateTime.now())
  }

  companion object {
    const val EXTRA_TOKEN: String = "extra_token"
  }
}