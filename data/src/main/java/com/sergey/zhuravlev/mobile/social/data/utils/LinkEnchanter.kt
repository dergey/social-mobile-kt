package com.sergey.zhuravlev.mobile.social.data.utils

object LinkEnchanter {

  fun enchant(pattern: String, host: String, vararg args: Any?): String {
    return pattern.format(host, *args)
  }
}