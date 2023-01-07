package com.sergey.zhuravlev.mobile.social.domain.model

import android.os.Parcelable
import com.sergey.zhuravlev.mobile.social.domain.enums.RelationshipStatus
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime

@Parcelize
data class ProfileModel(
  val username: String,
  val firstName: String,
  val middleName: String? = null,
  val secondName: String,
  val gender: String? = null,
  val city: String? = null,
  val birthDate: LocalDate? = null,
  val lastSeen: LocalDateTime? = null,
  val overview: String? = null,
  val relationshipStatus: RelationshipStatus? = null,
  val workplace: String? = null,
  val education: String? = null,
  val citizenship: String? = null,
  val registrationAddress: String? = null,
  val residenceAddress: String? = null,
  val createAt: LocalDateTime? = null,
  val updateAt: LocalDateTime? = null,
  val attitude: String,
  val avatarLink: String?
) : Parcelable {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ProfileModel

    if (username != other.username) return false
    if (firstName != other.firstName) return false
    if (middleName != other.middleName) return false
    if (secondName != other.secondName) return false
    if (gender != other.gender) return false
    if (city != other.city) return false
    if (birthDate != other.birthDate) return false
    if (lastSeen != other.lastSeen) return false
    if (overview != other.overview) return false
    if (relationshipStatus != other.relationshipStatus) return false
    if (workplace != other.workplace) return false
    if (education != other.education) return false
    if (citizenship != other.citizenship) return false
    if (registrationAddress != other.registrationAddress) return false
    if (residenceAddress != other.residenceAddress) return false
    if (createAt != other.createAt) return false
    if (updateAt != other.updateAt) return false
    if (attitude != other.attitude) return false
    if (avatarLink != other.avatarLink) return false

    return true
  }

  override fun hashCode(): Int {
    var result = username.hashCode()
    result = 31 * result + firstName.hashCode()
    result = 31 * result + (middleName?.hashCode() ?: 0)
    result = 31 * result + secondName.hashCode()
    result = 31 * result + (gender?.hashCode() ?: 0)
    result = 31 * result + (city?.hashCode() ?: 0)
    result = 31 * result + (birthDate?.hashCode() ?: 0)
    result = 31 * result + (lastSeen?.hashCode() ?: 0)
    result = 31 * result + (overview?.hashCode() ?: 0)
    result = 31 * result + (relationshipStatus?.hashCode() ?: 0)
    result = 31 * result + (workplace?.hashCode() ?: 0)
    result = 31 * result + (education?.hashCode() ?: 0)
    result = 31 * result + (citizenship?.hashCode() ?: 0)
    result = 31 * result + (registrationAddress?.hashCode() ?: 0)
    result = 31 * result + (residenceAddress?.hashCode() ?: 0)
    result = 31 * result + (createAt?.hashCode() ?: 0)
    result = 31 * result + (updateAt?.hashCode() ?: 0)
    result = 31 * result + attitude.hashCode()
    result = 31 * result + (avatarLink?.hashCode() ?: 0)
    return result
  }
}