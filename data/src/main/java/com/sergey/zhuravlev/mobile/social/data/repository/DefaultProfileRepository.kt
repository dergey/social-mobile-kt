package com.sergey.zhuravlev.mobile.social.data.repository

import com.sergey.zhuravlev.mobile.social.data.OperationErrorCodes
import com.sergey.zhuravlev.mobile.social.data.api.service.ProfileService
import com.sergey.zhuravlev.mobile.social.data.database.dao.ProfileDetailModelDao
import com.sergey.zhuravlev.mobile.social.data.database.dao.ProfileModelDao
import com.sergey.zhuravlev.mobile.social.data.mapper.ProfileEntityMapper
import com.sergey.zhuravlev.mobile.social.data.mapper.transform.toDomainModel
import com.sergey.zhuravlev.mobile.social.data.mapper.transform.toErrorModel
import com.sergey.zhuravlev.mobile.social.domain.model.ErrorModel
import com.sergey.zhuravlev.mobile.social.domain.model.Operation
import com.sergey.zhuravlev.mobile.social.domain.model.ProfileModel
import com.sergey.zhuravlev.mobile.social.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultProfileRepository(
  private val profileModelDao: ProfileModelDao,
  private val profileDetailModelDao: ProfileDetailModelDao,
  private val profileService: ProfileService
) : ProfileRepository {

  override suspend fun getCurrentUserProfile(): Operation<ProfileModel> {
    return withContext(Dispatchers.IO) {
      val response = profileService.getCurrentUserProfile()

      response.onSuccess {
        val currentProfile = ProfileEntityMapper.toEntity(it)
        profileModelDao.insert(currentProfile.profile)
        profileDetailModelDao.insert(currentProfile.detail)
        return@withContext Operation.of(currentProfile.toDomainModel())
      }

      return@withContext response.errorData?.let {
        Operation.error<ProfileModel>(it.toErrorModel())
      } ?: throw IllegalStateException("impossible")
    }
  }

  override suspend fun getCurrentUserProfileCache(): Operation<ProfileModel> {
    return withContext(Dispatchers.IO) {
      val profile = profileModelDao.getCurrentWithDetail()
      if (profile != null) {
        return@withContext Operation.of(profile.toDomainModel())
      } else {
        return@withContext Operation.error(ErrorModel(OperationErrorCodes.EMPTY_CACHE))
      }
    }
  }

  override suspend fun createOrUpdateProfileAvatar(filePath: String): Operation<ProfileModel> {
    TODO("Not yet implemented")
  }

  override suspend fun getProfile(username: String): Operation<ProfileModel> {
    return withContext(Dispatchers.IO) {
      val response = profileService.getProfile(username)

      response.onSuccess {
        return@withContext Operation.of(it.toDomainModel())
      }

      return@withContext response.errorData?.let {
        Operation.error<ProfileModel>(it.toErrorModel())
      } ?: throw IllegalStateException("impossible")
    }
  }
}
