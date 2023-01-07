package com.sergey.zhuravlev.mobile.social.data.api.service

import com.sergey.zhuravlev.mobile.social.data.api.dto.ContinuationDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.UserDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.confirmation.ManualCodeConfirmationDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.registration.CompleteRegistrationDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.registration.RegistrationStatusDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.registration.StartRegistrationDto
import com.sergey.zhuravlev.mobile.social.data.api.common.SocialResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface RegistrationService {

  @POST("/api/registration")
  suspend fun startRegistration(@Body body: StartRegistrationDto): SocialResponse<RegistrationStatusDto>

  @POST("/api/registration/confirm/code")
  suspend fun confirmByCode(@Body body: ManualCodeConfirmationDto): SocialResponse<RegistrationStatusDto>

  @POST("/api/registration/confirm/link")
  suspend fun confirmByLink(@Query("linkCode") linkCode: String): SocialResponse<RegistrationStatusDto>

  @POST("/api/registration/resend")
  suspend fun resendConfirmation(@Body body: ContinuationDto): SocialResponse<RegistrationStatusDto>

  @POST("/api/registration/complete")
  suspend fun completeRegistration(@Body body: CompleteRegistrationDto): SocialResponse<UserDto>

}