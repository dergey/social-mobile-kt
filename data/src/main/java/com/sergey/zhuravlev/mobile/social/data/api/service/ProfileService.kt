package com.sergey.zhuravlev.mobile.social.data.api.service

import com.sergey.zhuravlev.mobile.social.data.api.common.SocialResponse
import com.sergey.zhuravlev.mobile.social.data.api.dto.ProfileDto
import okhttp3.MultipartBody
import retrofit2.http.*

interface ProfileService {

  @GET("/api/profile")
  suspend fun getCurrentUserProfile(): SocialResponse<ProfileDto>

  @Multipart
  @POST("/api/profile/avatar")
  suspend fun createOrUpdateProfileAvatar(
    @Part body: MultipartBody.Part
  ): SocialResponse<ProfileDto>

  @GET("/api/profile/{username}")
  suspend fun getProfile(@Path("username") username: String): SocialResponse<ProfileDto>

//  @GET("/api/profile/{username}/avatar")
//  suspend fun getProfileAvatar(): Call<Void>

}