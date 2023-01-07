package com.sergey.zhuravlev.mobile.social.data.api.dto

import com.sergey.zhuravlev.mobile.social.data.api.enums.PrivacyScope

data class UpdatePrivatePropertyDto<T>(
  val value: T,
  val privacyRule: PrivacyScope,
)