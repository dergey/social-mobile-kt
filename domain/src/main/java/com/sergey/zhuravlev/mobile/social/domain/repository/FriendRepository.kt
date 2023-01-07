package com.sergey.zhuravlev.mobile.social.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.sergey.zhuravlev.mobile.social.domain.model.Operation
import com.sergey.zhuravlev.mobile.social.domain.model.ProfileModel

interface FriendRepository {

  fun getCurrentUserFriends(): LiveData<PagingData<ProfileModel>>

  fun getProfileFriends(username: String): LiveData<PagingData<ProfileModel>>

  suspend fun removeFriend(username: String): Operation<Void>

  fun getCurrentUserIncomingFriendRequests(): LiveData<PagingData<ProfileModel>>

  fun getCurrentUserOutgoingFriendRequests(): LiveData<PagingData<ProfileModel>>

  suspend fun createFriendRequest(username: String): Operation<Void>

  suspend fun revokeFriendRequest(username: String): Operation<Void>

  suspend fun acceptFriendRequest(username: String): Operation<Void>

  suspend fun declineFriendRequest(username: String): Operation<Void>
}