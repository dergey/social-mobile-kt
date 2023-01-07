package com.sergey.zhuravlev.mobile.social.data.api.service

import com.sergey.zhuravlev.mobile.social.data.api.dto.PageDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.ProfilePreviewDto
import com.sergey.zhuravlev.mobile.social.data.api.common.Sort
import com.sergey.zhuravlev.mobile.social.data.api.common.SocialResponse
import retrofit2.http.*

interface FriendService {

  @GET("/api/friend")
  suspend fun getCurrentUserFriends(
    @Query("page") page: Int?,
    @Query("size") size: Int?,
    @Query("sort") sort: Sort? = null
  ): SocialResponse<PageDto<ProfilePreviewDto>>

  @GET("/api/profile/{username}/friend")
  suspend fun getProfileFriends(
    @Path("username") username: String,
    @Query("page") page: Int?,
    @Query("size") size: Int?,
    @Query("sort") sort: Sort? = null
  ): SocialResponse<PageDto<ProfilePreviewDto>>

  @DELETE("/api/friend/{username}")
  suspend fun removeFriend(@Path("username") username: String): SocialResponse<Void>

  @GET("/api/friend/incoming")
  suspend fun getCurrentUserIncomingFriendRequests(
    @Query("page") page: Int?,
    @Query("size") size: Int?,
    @Query("sort") sort: Sort? = null
  ): SocialResponse<PageDto<ProfilePreviewDto>>

  @GET("/api/friend/outgoing")
  suspend fun getCurrentUserOutgoingFriendRequests(
    @Query("page") page: Int?,
    @Query("size") size: Int?,
    @Query("sort") sort: Sort? = null
  ): SocialResponse<PageDto<ProfilePreviewDto>>

  @POST("/api/profile/{username}/friend/request")
  suspend fun createFriendRequest(@Path("username") username: String): SocialResponse<Void>

  @DELETE("/api/profile/{username}/friend/request")
  suspend fun revokeFriendRequest(@Path("username") username: String): SocialResponse<Void>

  @POST("/api/friend/incoming/{username}/accept")
  suspend fun acceptFriendRequest(@Path("username") username: String): SocialResponse<Void>

  @POST("/api/friend/incoming/{username}/decline")
  suspend fun declineFriendRequest(@Path("username") username: String): SocialResponse<Void>

}