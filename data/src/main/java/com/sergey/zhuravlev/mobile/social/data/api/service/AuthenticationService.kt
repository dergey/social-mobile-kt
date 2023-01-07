package com.sergey.zhuravlev.mobile.social.data.api.service

import com.sergey.zhuravlev.mobile.social.data.api.common.SocialResponse
import com.sergey.zhuravlev.mobile.social.data.api.dto.LoginDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {

  @POST("/api/authenticate")
  suspend fun authenticate(@Body body: LoginDto): SocialResponse<LoginResponseDto>

}