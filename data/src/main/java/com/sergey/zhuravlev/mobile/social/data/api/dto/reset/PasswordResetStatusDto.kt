package com.sergey.zhuravlev.mobile.social.data.api.dto.reset

import com.sergey.zhuravlev.mobile.social.data.api.enums.PasswordResetStatus
import java.util.*

data class PasswordResetStatusDto(
  val status: PasswordResetStatus,
  val continuationCode: UUID,
)