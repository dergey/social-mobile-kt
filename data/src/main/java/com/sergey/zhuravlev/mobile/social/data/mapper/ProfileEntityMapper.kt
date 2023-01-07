package com.sergey.zhuravlev.mobile.social.data.mapper

import com.sergey.zhuravlev.mobile.social.data.api.dto.AddressDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.ProfileDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.ProfilePreviewDto
import com.sergey.zhuravlev.mobile.social.data.database.entity.ProfileAndDetailEntities
import com.sergey.zhuravlev.mobile.social.data.database.entity.ProfileDetailEntity
import com.sergey.zhuravlev.mobile.social.data.database.entity.ProfileEntity
import com.sergey.zhuravlev.mobile.social.data.database.entity.ProfilePreviewEmbeddable

object ProfileEntityMapper {

  fun toEntity(dto: ProfileDto): ProfileAndDetailEntities {
    return ProfileAndDetailEntities(
      profile = ProfileEntity(
        username = dto.username,
        firstName = dto.firstName,
        middleName = dto.middleName,
        secondName = dto.secondName,
        gender = dto.gender?.name,
        city = dto.residenceAddress?.city,
        birthDate = dto.birthDate,
        attitude = dto.attitude.name
      ),
      detail = ProfileDetailEntity(
        username = dto.username,
        overview = dto.overview,
        relationshipStatus = dto.relationshipStatus?.name,
        workplace = dto.workplace,
        education = dto.education,
        citizenship = dto.citizenship,
        registrationAddress = convertAddress(dto.registrationAddress),
        residenceAddress = convertAddress(dto.residenceAddress),
        createAt = dto.createAt,
        updateAt = dto.updateAt
      )
    )
  }

  fun toEntity(dto: ProfilePreviewDto): ProfileEntity {
    return ProfileEntity(
      username = dto.username,
      firstName = dto.firstName,
      middleName = dto.middleName,
      secondName = dto.secondName,
      birthDate = dto.birthDate,
      attitude = dto.attitude.name
    )
  }

  fun toEmbeddable(dto: ProfilePreviewDto): ProfilePreviewEmbeddable {
    return ProfilePreviewEmbeddable(
      username = dto.username,
      firstName = dto.firstName,
      middleName = dto.middleName,
      secondName = dto.secondName,
      city = dto.city,
      birthDate = dto.birthDate,
      attitude = dto.attitude.name
    )
  }

  private fun convertAddress(address: AddressDto?): String? {
    return address?.let {
      "${it.country}, ${it.city ?: ""}, ${it.firstLine ?: ""} ${it.secondLine ?: ""}," +
          " ${it.zipCode ?: ""}"
    }
  }
}