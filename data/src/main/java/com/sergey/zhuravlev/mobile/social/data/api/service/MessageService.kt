package com.sergey.zhuravlev.mobile.social.data.api.service

import com.sergey.zhuravlev.mobile.social.data.api.common.SocialResponse
import com.sergey.zhuravlev.mobile.social.data.api.common.Sort
import com.sergey.zhuravlev.mobile.social.data.api.dto.PageDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.message.CreateMessageDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.message.MessageDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.message.UpdateTextMessageDto
import okhttp3.MultipartBody
import retrofit2.http.*

interface MessageService {

  @GET("/api/chat/{chatId}/message")
  suspend fun getChatMessages(
    @Path("chatId") chatId: Long,
    @Query("page") page: Int?,
    @Query("size") size: Int?,
    @Query("sort") sort: Sort? = null
  ): SocialResponse<PageDto<MessageDto>>

  @POST("/api/chat/{chatId}/message")
  suspend fun createMessage(
    @Path("chatId") chatId: Long,
    @Body body: CreateMessageDto
  ): SocialResponse<MessageDto>

  @Multipart
  @POST("/api/chat/{chatId}/message/image")
  suspend fun createImageMessage(
    @Path("chatId") chatId: Long,
    @Part image: MultipartBody.Part
  ): SocialResponse<MessageDto>

  @PUT("/api/chat/{chatId}/message/{messageId}")
  suspend fun updateTextMessage(
    @Path("chatId") chatId: Long,
    @Path("messageId") messageId: Long,
    @Body body: UpdateTextMessageDto
  ): SocialResponse<MessageDto>

  @DELETE("/api/chat/{chatId}/message/{messageId}")
  suspend fun deleteMessage(
    @Path("chatId") chatId: Long,
    @Path("messageId") messageId: Long
  ): SocialResponse<Void>

//  @GET("/api/chat/{chatId}/message/{messageId}/image")
//  fun getMessageImage(): SocialResponse<Void>

}