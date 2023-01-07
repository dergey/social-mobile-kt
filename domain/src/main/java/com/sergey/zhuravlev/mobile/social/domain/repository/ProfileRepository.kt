package com.sergey.zhuravlev.mobile.social.domain.repository

import com.sergey.zhuravlev.mobile.social.domain.model.Operation
import com.sergey.zhuravlev.mobile.social.domain.model.ProfileModel

interface ProfileRepository {

  suspend fun getCurrentUserProfile(): Operation<ProfileModel>

  suspend fun getCurrentUserProfileCache(): Operation<ProfileModel>

  suspend fun createOrUpdateProfileAvatar(filePath: String): Operation<ProfileModel>

  suspend fun getProfile(username: String): Operation<ProfileModel>
}