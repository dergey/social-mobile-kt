package com.sergey.zhuravlev.mobile.social.data.api.dto.reset

import java.util.*

data class CompletePasswordResetDto(
    val continuationCode: UUID,
    val password: String,
)