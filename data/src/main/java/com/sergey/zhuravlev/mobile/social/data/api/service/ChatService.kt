package com.sergey.zhuravlev.mobile.social.data.api.service

import com.sergey.zhuravlev.mobile.social.data.api.dto.*
import com.sergey.zhuravlev.mobile.social.data.api.common.SocialResponse
import com.sergey.zhuravlev.mobile.social.data.api.common.Sort
import retrofit2.http.*

interface ChatService {

  @GET("/api/chat")
  suspend fun getCurrentUserChats(
    @Query("page") page: Int?,
    @Query("size") size: Int?,
    @Query("sort") sort: Sort? = null
  ): SocialResponse<PageDto<ChatPreviewDto>>

  @POST("/api/chat")
  suspend fun getOrCreateChat(@Body body: CreateChatDto): SocialResponse<ChatDto>

  @POST("/api/chat/{id}/read")
  suspend fun updateReadStatus(@Path("id") chatId: Long): SocialResponse<Void>

  @POST("/api/chat/{id}/block")
  suspend fun blockChat(@Path("id") chatId: Long): SocialResponse<ChatDto>

  @POST("/api/chat/{id}/unblock")
  suspend fun unblockChat(@Path("id") chatId: Long): SocialResponse<ChatDto>

}