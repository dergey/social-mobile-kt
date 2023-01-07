package com.sergey.zhuravlev.mobile.social.data.api.service

import com.sergey.zhuravlev.mobile.social.data.api.common.SocialResponse
import com.sergey.zhuravlev.mobile.social.data.api.dto.ContinuationDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.confirmation.ManualCodeConfirmationDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.reset.CompletePasswordResetDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.reset.PasswordResetStatusDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.reset.StartPasswordResetDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface PasswordResetService {

  @POST("/api/password-reset")
  suspend fun startPasswordReset(@Body body: StartPasswordResetDto): SocialResponse<PasswordResetStatusDto>

  @POST("/api/password-reset/confirm/code")
  suspend fun confirmByCode(@Body body: ManualCodeConfirmationDto): SocialResponse<PasswordResetStatusDto>

  @POST("/api/password-reset/confirm/link")
  suspend fun confirmByLink(@Query("linkCode") linkCode: String): SocialResponse<PasswordResetStatusDto>

  @POST("/api/password-reset/resend")
  suspend fun resendConfirmation(@Body body: ContinuationDto): SocialResponse<PasswordResetStatusDto>

  @POST("/api/password-reset/complete")
  suspend fun completePasswordReset(@Body body: CompletePasswordResetDto): SocialResponse<Void>

}