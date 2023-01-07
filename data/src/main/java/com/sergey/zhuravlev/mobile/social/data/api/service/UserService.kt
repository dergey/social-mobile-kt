package com.sergey.zhuravlev.mobile.social.data.api.service

import com.sergey.zhuravlev.mobile.social.data.api.dto.UserDto
import com.sergey.zhuravlev.mobile.social.data.api.common.SocialResponse
import retrofit2.http.GET

interface UserService {

  @GET("/api/user")
  suspend fun getCurrentUser(): SocialResponse<UserDto>

}