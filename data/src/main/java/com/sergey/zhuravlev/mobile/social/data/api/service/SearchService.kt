package com.sergey.zhuravlev.mobile.social.data.api.service

import com.sergey.zhuravlev.mobile.social.data.api.dto.PageDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.ProfilePreviewDto
import com.sergey.zhuravlev.mobile.social.data.api.common.Sort
import com.sergey.zhuravlev.mobile.social.data.api.common.SocialResponse
import com.sergey.zhuravlev.mobile.social.data.api.enums.RelationshipStatus
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

  @GET("/api/search")
  suspend fun searchProfile(
    @Query("query") query: String?,
    @Query("country") country: String?,
    @Query("city") city: String?,
    @Query("relationshipStatus") relationshipStatus: RelationshipStatus?,
    @Query("age") age: Int?,
    @Query("ageFrom") ageFrom: Int?,
    @Query("ageTo") ageTo: Int?,
    @Query("page") page: Int?,
    @Query("size") size: Int?,
    @Query("sort") sort: Sort? = null
  ): SocialResponse<PageDto<ProfilePreviewDto>>

}