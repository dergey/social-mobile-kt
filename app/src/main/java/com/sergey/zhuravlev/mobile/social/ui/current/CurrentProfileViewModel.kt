package com.sergey.zhuravlev.mobile.social.ui.current

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sergey.zhuravlev.mobile.social.data.api.common.ErrorData
import com.sergey.zhuravlev.mobile.social.domain.model.ErrorModel
import com.sergey.zhuravlev.mobile.social.domain.model.ProfileModel
import com.sergey.zhuravlev.mobile.social.domain.repository.FriendRepository
import com.sergey.zhuravlev.mobile.social.domain.repository.ProfileRepository

class CurrentProfileViewModel(
  profileRepository: ProfileRepository,
  friendRepository: FriendRepository,
) : ViewModel() {

  private val _error = MutableLiveData<ErrorModel>()

  val error: LiveData<ErrorModel> = _error

  val currentProfile: LiveData<ProfileModel> = liveData {
    val cache = profileRepository.getCurrentUserProfileCache()
    if (cache.success) {
      emit(cache.data!!)
    }
    val operation = profileRepository.getCurrentUserProfile()
    if (operation.success) {
      emit(operation.data!!)
    } else {
      _error.value = operation.errorData!!
    }
  }

  val friends: LiveData<PagingData<ProfileModel>> =
    friendRepository.getCurrentUserFriends().cachedIn(viewModelScope)

  val incomingFriendRequests: LiveData<PagingData<ProfileModel>> =
    friendRepository.getCurrentUserIncomingFriendRequests().cachedIn(viewModelScope)

  val outgoingFriendRequests: LiveData<PagingData<ProfileModel>> =
    friendRepository.getCurrentUserOutgoingFriendRequests().cachedIn(viewModelScope)
}