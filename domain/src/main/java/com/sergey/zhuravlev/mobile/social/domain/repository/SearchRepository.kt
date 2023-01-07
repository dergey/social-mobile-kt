package com.sergey.zhuravlev.mobile.social.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.sergey.zhuravlev.mobile.social.domain.enums.RelationshipStatus
import com.sergey.zhuravlev.mobile.social.domain.model.ProfileModel

interface SearchRepository {

  fun searchProfiles(
    query: String?,
    country: String?,
    city: String?,
    relationshipStatus: RelationshipStatus?,
    age: Int?,
    ageFrom: Int?,
    ageTo: Int?,
    pageSize: Int = 20,
    enablePlaceholders: Boolean = false
  ): LiveData<PagingData<ProfileModel>>

}