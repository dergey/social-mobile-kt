package com.sergey.zhuravlev.mobile.social.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.*
import com.sergey.zhuravlev.mobile.social.data.api.service.FriendService
import com.sergey.zhuravlev.mobile.social.data.api.service.SearchService
import com.sergey.zhuravlev.mobile.social.data.mapper.transform.toDomainModel
import com.sergey.zhuravlev.mobile.social.data.mapper.transform.toErrorModel
import com.sergey.zhuravlev.mobile.social.data.paging.CommonPagingSource
import com.sergey.zhuravlev.mobile.social.domain.enums.RelationshipStatus
import com.sergey.zhuravlev.mobile.social.domain.model.Operation
import com.sergey.zhuravlev.mobile.social.domain.model.ProfileModel
import com.sergey.zhuravlev.mobile.social.domain.repository.FriendRepository
import com.sergey.zhuravlev.mobile.social.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultSearchRepository(
  private val searchService: SearchService
) : SearchRepository {

  override fun searchProfiles(
    query: String?,
    country: String?,
    city: String?,
    relationshipStatus: RelationshipStatus?,
    age: Int?,
    ageFrom: Int?,
    ageTo: Int?,
    pageSize: Int,
    enablePlaceholders: Boolean
  ): LiveData<PagingData<ProfileModel>> {
    return Pager(
      config = PagingConfig(
        pageSize = pageSize,
        enablePlaceholders = enablePlaceholders
      ),
      pagingSourceFactory = {
        CommonPagingSource(
          pageSize,
          mapOf(
            "query" to query,
            "country" to country,
            "city" to city,
            "relationshipStatus" to relationshipStatus,
            "age" to age,
            "ageFrom" to ageFrom,
            "ageTo" to ageTo
          )
        ) {
          searchService.searchProfile(
            query = it.getPayloadString("query"),
            country = it.getPayloadString("country"),
            city = it.getPayloadString("city"),
            relationshipStatus = it.getPayload("relationshipStatus"),
            age = it.getPayloadInt("age"),
            ageFrom = it.getPayloadInt("ageFrom"),
            ageTo = it.getPayloadInt("ageTo"),
            page = it.pageNumber,
            size = it.pageSize
          )
        }
      }
    ).liveData
      .map { pagingData ->
        pagingData.map { it.toDomainModel() }
      }
  }
}