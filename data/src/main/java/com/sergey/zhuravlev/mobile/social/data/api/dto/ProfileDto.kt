package com.sergey.zhuravlev.mobile.social.data.api.dto

import com.sergey.zhuravlev.mobile.social.data.api.enums.Gender
import com.sergey.zhuravlev.mobile.social.data.api.enums.ProfileAttitude
import com.sergey.zhuravlev.mobile.social.data.api.enums.RelationshipStatus
import java.time.LocalDate
import java.time.LocalDateTime

data class ProfileDto(
  val username: String,
  val avatar: ImageDto?,
  val firstName: String,
  val middleName: String?,
  val secondName: String,
  val gender: Gender?,
  val birthDate: LocalDate?,
  val lastSeen: LocalDateTime?,
  val overview: String?,
  val relationshipStatus: RelationshipStatus?,
  val workplace: String?,
  val education: String?,
  val citizenship: String?,
  val registrationAddress: AddressDto?,
  val residenceAddress: AddressDto?,
  val createAt: LocalDateTime,
  val updateAt: LocalDateTime,
  val attitude: ProfileAttitude,
)