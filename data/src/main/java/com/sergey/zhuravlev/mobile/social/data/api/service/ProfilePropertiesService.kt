package com.sergey.zhuravlev.mobile.social.data.api.service

import com.sergey.zhuravlev.mobile.social.data.api.common.SocialResponse
import com.sergey.zhuravlev.mobile.social.data.api.dto.*
import com.sergey.zhuravlev.mobile.social.data.api.enums.RelationshipStatus
import retrofit2.http.Body
import retrofit2.http.PUT
import java.time.LocalDate

interface ProfilePropertiesService {

  @PUT("/api/profile/property/submission")
  suspend fun updateSubmission(@Body body: UpdatePropertyDto<ProfileSubmissionDto>): SocialResponse<ProfileDto>

  @PUT("/api/profile/property/username")
  suspend fun updateUsername(@Body body: UpdatePropertyDto<String>): SocialResponse<ProfileDto>

  @PUT("/api/profile/property/birthDate")
  suspend fun updateBirthDate(@Body body: UpdatePrivatePropertyDto<LocalDate>): SocialResponse<ProfileDto>

  @PUT("/api/profile/property/overview")
  suspend fun updateOverview(@Body body: UpdatePropertyDto<String>): SocialResponse<ProfileDto>

  @PUT("/api/profile/property/relationshipStatus")
  suspend fun updateRelationshipStatus(@Body body: UpdatePropertyDto<RelationshipStatus>): SocialResponse<ProfileDto>

  @PUT("/api/profile/property/workplace")
  suspend fun updateWorkplace(@Body body: UpdatePrivatePropertyDto<String>): SocialResponse<ProfileDto>

  @PUT("/api/profile/property/citizenship")
  suspend fun updateCitizenship(@Body body: UpdatePrivatePropertyDto<String>): SocialResponse<ProfileDto>

  @PUT("/api/profile/property/education")
  suspend fun updateEducation(@Body body: UpdatePrivatePropertyDto<EducationDto>): SocialResponse<ProfileDto>

  @PUT("/api/profile/property/addresses")
  suspend fun updateAddresses(@Body body: List<UpdatePrivatePropertyDto<UpdateAddressDto>>): SocialResponse<ProfileDto>

}