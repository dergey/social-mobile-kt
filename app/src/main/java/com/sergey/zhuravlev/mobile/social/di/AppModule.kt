package com.sergey.zhuravlev.mobile.social.di

import com.sergey.zhuravlev.mobile.social.ui.chat.ChatListViewModel
import com.sergey.zhuravlev.mobile.social.ui.current.CurrentProfileViewModel
import com.sergey.zhuravlev.mobile.social.ui.login.LoginViewModel
import com.sergey.zhuravlev.mobile.social.ui.message.MessageListViewModel
import com.sergey.zhuravlev.mobile.social.ui.profile.ProfileViewModel
import com.sergey.zhuravlev.mobile.social.ui.search.SearchProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

  viewModel<CurrentProfileViewModel> {
    CurrentProfileViewModel(get(), get())
  }

  viewModel<SearchProfileViewModel> {
    SearchProfileViewModel(get())
  }

  viewModel<ChatListViewModel> {
    ChatListViewModel(get())
  }

  viewModel<MessageListViewModel> {
    MessageListViewModel(get(), get(), get())
  }

  viewModel<ProfileViewModel> {
    ProfileViewModel(get(), get())
  }

  viewModel<LoginViewModel> {
    LoginViewModel(get(), get())
  }

}
