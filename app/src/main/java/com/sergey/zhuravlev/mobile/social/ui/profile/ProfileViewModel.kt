package com.sergey.zhuravlev.mobile.social.ui.profile

import android.content.Intent
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sergey.zhuravlev.mobile.social.domain.model.ErrorModel
import com.sergey.zhuravlev.mobile.social.domain.model.ProfileModel
import com.sergey.zhuravlev.mobile.social.domain.repository.FriendRepository
import com.sergey.zhuravlev.mobile.social.domain.repository.ProfileRepository
import kotlin.properties.Delegates

class ProfileViewModel(
  profileRepository: ProfileRepository,
  friendRepository: FriendRepository,
) : ViewModel() {

  private var username by Delegates.notNull<String>()

  private val _error = MutableLiveData<ErrorModel>()

  val error: LiveData<ErrorModel> = _error

  val profile: LiveData<ProfileModel> by lazy {
    liveData {
      val operation = profileRepository.getProfile(username)
      if (operation.success) {
        emit(operation.data!!)
      } else {
        _error.value = operation.errorData!!
      }
    }
  }

  val profileFriends: LiveData<PagingData<ProfileModel>> by lazy {
    friendRepository.getProfileFriends(username).cachedIn(viewModelScope)
  }

  fun parseIntent(intent: Intent) {
    val username: String? = intent.getStringExtra(ProfileActivity.INTENT_ARG_USERNAME)
    this.username = username!!
  }
}