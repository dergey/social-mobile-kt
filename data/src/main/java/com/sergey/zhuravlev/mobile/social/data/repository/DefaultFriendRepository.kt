package com.sergey.zhuravlev.mobile.social.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.*
import com.sergey.zhuravlev.mobile.social.data.api.service.FriendService
import com.sergey.zhuravlev.mobile.social.data.mapper.transform.toDomainModel
import com.sergey.zhuravlev.mobile.social.data.mapper.transform.toErrorModel
import com.sergey.zhuravlev.mobile.social.data.paging.CommonPagingSource
import com.sergey.zhuravlev.mobile.social.domain.model.Operation
import com.sergey.zhuravlev.mobile.social.domain.model.ProfileModel
import com.sergey.zhuravlev.mobile.social.domain.repository.FriendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultFriendRepository(
  private val friendService: FriendService
) : FriendRepository {

  override fun getCurrentUserFriends(): LiveData<PagingData<ProfileModel>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20,
        enablePlaceholders = true
      ),
      pagingSourceFactory = {
        CommonPagingSource(
          20,
          emptyMap()
        ) {
          friendService.getCurrentUserFriends(
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

  override fun getProfileFriends(username: String): LiveData<PagingData<ProfileModel>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20,
        enablePlaceholders = true
      ),
      pagingSourceFactory = {
        CommonPagingSource(
          20,
          mapOf("username" to username)
        ) {
          friendService.getProfileFriends(
            username = it.requirePayloadString("username"),
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

  override suspend fun removeFriend(username: String): Operation<Void> {
    return withContext(Dispatchers.IO) {
      val response = friendService.removeFriend(username)

      response.onSuccess {
        return@withContext Operation.success()
      }

      return@withContext response.errorData?.let {
        Operation.error<Void>(it.toErrorModel())
      } ?: throw IllegalStateException("impossible")
    }
  }

  override fun getCurrentUserIncomingFriendRequests(): LiveData<PagingData<ProfileModel>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20,
        enablePlaceholders = false
      ),
      pagingSourceFactory = {
        CommonPagingSource(
          20,
          emptyMap()
        ) {
          friendService.getCurrentUserIncomingFriendRequests(
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

  override fun getCurrentUserOutgoingFriendRequests(): LiveData<PagingData<ProfileModel>> {
    return Pager(
      config = PagingConfig(
        pageSize = 20,
        enablePlaceholders = false
      ),
      pagingSourceFactory = {
        CommonPagingSource(
          20,
          emptyMap()
        ) {
          friendService.getCurrentUserOutgoingFriendRequests(
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

  override suspend fun createFriendRequest(username: String): Operation<Void> {
    return withContext(Dispatchers.IO) {
      val response = friendService.createFriendRequest(username)

      response.onSuccess {
        return@withContext Operation.success()
      }

      return@withContext response.errorData?.let {
        Operation.error<Void>(it.toErrorModel())
      } ?: throw IllegalStateException("impossible")
    }
  }

  override suspend fun revokeFriendRequest(username: String): Operation<Void> {
    return withContext(Dispatchers.IO) {
      val response = friendService.revokeFriendRequest(username)

      response.onSuccess {
        return@withContext Operation.success()
      }

      return@withContext response.errorData?.let {
        Operation.error<Void>(it.toErrorModel())
      } ?: throw IllegalStateException("impossible")
    }
  }

  override suspend fun acceptFriendRequest(username: String): Operation<Void> {
    return withContext(Dispatchers.IO) {
      val response = friendService.acceptFriendRequest(username)

      response.onSuccess {
        return@withContext Operation.success()
      }

      return@withContext response.errorData?.let {
        Operation.error<Void>(it.toErrorModel())
      } ?: throw IllegalStateException("impossible")
    }
  }

  override suspend fun declineFriendRequest(username: String): Operation<Void> {
    return withContext(Dispatchers.IO) {
      val response = friendService.declineFriendRequest(username)

      response.onSuccess {
        return@withContext Operation.success()
      }

      return@withContext response.errorData?.let {
        Operation.error<Void>(it.toErrorModel())
      } ?: throw IllegalStateException("impossible")
    }
  }
}