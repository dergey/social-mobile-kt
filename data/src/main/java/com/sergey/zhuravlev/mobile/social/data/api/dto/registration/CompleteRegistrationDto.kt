package com.sergey.zhuravlev.mobile.social.data.api.dto.registration

import com.sergey.zhuravlev.mobile.social.data.api.enums.Gender
import java.time.LocalDate
import java.util.*

data class CompleteRegistrationDto(
    val continuationCode: UUID,
    val password: String,
    val username: String,
    val firstName: String,
    val middleName: String?,
    val secondName: String,
    val gender: Gender,
    val birthDate: LocalDate?
)