package com.sergey.zhuravlev.mobile.social.data.api.service

import com.sergey.zhuravlev.mobile.social.data.api.dto.sticker.CreateStickerPackDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.sticker.StickerDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.sticker.StickerPackDto
import com.sergey.zhuravlev.mobile.social.data.api.common.SocialResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface StickerService {

  @GET("/api/sticker")
  suspend fun getStickerPackBySticker(@Query("stickerId") stickerId: Long?): SocialResponse<StickerPackDto>

  @GET("/api/sticker/{stickerPackId}")
  suspend fun getStickerPack(@Path("stickerPackId") stickerPackId: Long): SocialResponse<StickerPackDto>

  @POST("/api/sticker")
  suspend fun createStickerPack(@Body body: CreateStickerPackDto): SocialResponse<StickerPackDto>

  @DELETE("/api/sticker/{stickerPackId}")
  suspend fun deleteStrikerPack(@Path("stickerPackId") stickerPackId: Long): SocialResponse<Void>

  @GET("/api/sticker/{stickerPackId}/sticker")
  suspend fun getStickerPackStickers(@Path("stickerPackId") stickerPackId: Long): SocialResponse<List<StickerDto>>

  @Multipart
  @POST("/api/sticker/{stickerPackId}/sticker")
  suspend fun addStickerPackSticker(
    @Path("stickerPackId") stickerPackId: Long,
    @Part body: MultipartBody.Part
  ): SocialResponse<StickerDto>

  @DELETE("/api/sticker/{stickerPackId}/sticker/{stickerId}")
  suspend fun deleteStickerPackSticker(
    @Path("stickerPackId") stickerPackId: Long,
    @Path("stickerId") stickerId: Long
  ): SocialResponse<Void>

}