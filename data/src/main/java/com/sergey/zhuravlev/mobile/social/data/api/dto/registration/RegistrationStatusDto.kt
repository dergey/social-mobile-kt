package com.sergey.zhuravlev.mobile.social.data.api.dto.registration

import com.sergey.zhuravlev.mobile.social.data.api.enums.RegistrationStatus
import java.util.*

data class RegistrationStatusDto(
    val status: RegistrationStatus,
    val continuationCode: UUID,
)