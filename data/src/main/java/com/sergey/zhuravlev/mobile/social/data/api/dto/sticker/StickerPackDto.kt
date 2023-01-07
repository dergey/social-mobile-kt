package com.sergey.zhuravlev.mobile.social.data.api.dto.sticker

import com.sergey.zhuravlev.mobile.social.data.api.dto.ProfilePreviewDto

data class StickerPackDto(
    val id: Long,
    val author: ProfilePreviewDto,
    val title: String,
    val stickerTotal: Int,
)