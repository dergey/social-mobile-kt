package com.sergey.zhuravlev.mobile.social.data.mapper.transform

import com.sergey.zhuravlev.mobile.social.data.api.SocialApi
import com.sergey.zhuravlev.mobile.social.data.api.dto.AddressDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.ProfileDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.ProfilePreviewDto
import com.sergey.zhuravlev.mobile.social.data.utils.LinkEnchanter
import com.sergey.zhuravlev.mobile.social.domain.enums.RelationshipStatus
import com.sergey.zhuravlev.mobile.social.domain.model.ProfileModel

fun ProfilePreviewDto.toDomainModel(): ProfileModel {
  return ProfileModel(
    username = this.username,
    firstName = this.firstName,
    middleName = this.middleName,
    secondName = this.secondName,
    city = this.city,
    birthDate = this.birthDate,
    attitude = this.attitude.name,
    avatarLink = LinkEnchanter.enchant(SocialApi.PROFILE_AVATAR_PATTERN, SocialApi.BASE_URL, this.username)
  )
}

fun ProfileDto.toDomainModel(): ProfileModel {
  return ProfileModel(
    username = this.username,
    firstName = this.firstName,
    middleName = this.middleName,
    secondName = this.secondName,
    gender = this.gender?.name,
    city = this.residenceAddress?.city ?: this.registrationAddress?.city,
    birthDate = this.birthDate,
    lastSeen = this.lastSeen,
    overview = this.overview,
    relationshipStatus = this.relationshipStatus?.let { RelationshipStatus.valueOf(it.name) },
    workplace = this.workplace,
    education = this.education,
    citizenship = this.citizenship,
    registrationAddress = convertAddress(this.registrationAddress),
    residenceAddress = convertAddress(this.residenceAddress),
    createAt = this.createAt,
    updateAt = this.updateAt,
    attitude = this.attitude.name,
    avatarLink = LinkEnchanter.enchant(SocialApi.PROFILE_AVATAR_PATTERN, SocialApi.BASE_URL, this.username)
  )
}

private fun convertAddress(address: AddressDto?): String? {
  return address?.let {
    "${it.country}, ${it.city ?: ""}, ${it.firstLine ?: ""} ${it.secondLine ?: ""}," +
        " ${it.zipCode ?: ""}"
  }
}