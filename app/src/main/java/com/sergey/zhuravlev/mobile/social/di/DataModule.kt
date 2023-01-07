package com.sergey.zhuravlev.mobile.social.di

import com.sergey.zhuravlev.mobile.social.data.api.SocialApi
import com.sergey.zhuravlev.mobile.social.data.api.service.*
import com.sergey.zhuravlev.mobile.social.data.database.AppDatabase
import com.sergey.zhuravlev.mobile.social.data.database.dao.ChatModelDao
import com.sergey.zhuravlev.mobile.social.data.database.dao.MessageModelDao
import com.sergey.zhuravlev.mobile.social.data.database.dao.ProfileDetailModelDao
import com.sergey.zhuravlev.mobile.social.data.database.dao.ProfileModelDao
import com.sergey.zhuravlev.mobile.social.data.datasource.BarrierTokenDataSource
import com.sergey.zhuravlev.mobile.social.data.datasource.GlideDataSource
import com.sergey.zhuravlev.mobile.social.data.model.GlideCompressedImage
import com.sergey.zhuravlev.mobile.social.data.repository.*
import com.sergey.zhuravlev.mobile.social.domain.repository.*
import org.koin.dsl.module

val dataModule = module {

  single<AuthenticationService> {
    SocialApi.authenticationService
  }

  single<ChatService> {
    SocialApi.chatService
  }

  single<FriendService> {
    SocialApi.friendService
  }

  single<MessageService> {
    SocialApi.messageService
  }

  single<PasswordResetService> {
    SocialApi.passwordResetService
  }

  single<ProfilePropertiesService> {
    SocialApi.profilePropertiesService
  }

  single<ProfileService> {
    SocialApi.profileService
  }

  single<RegistrationService> {
    SocialApi.registrationService
  }

  single<SearchService> {
    SocialApi.searchService
  }

  single<StickerService> {
    SocialApi.stickerService
  }

  single<UserService> {
    SocialApi.userService
  }

  single<AppDatabase> {
    AppDatabase.buildDatabase(get())
  }

  single<ChatModelDao> {
    get<AppDatabase>().chatModelDao()
  }

  single<MessageModelDao> {
    get<AppDatabase>().messageModelDao()
  }

  single<ProfileDetailModelDao> {
    get<AppDatabase>().profileDetailModelDao()
  }

  single<ProfileModelDao> {
    get<AppDatabase>().profileModelDao()
  }

  single<GlideDataSource> {
    GlideDataSource(get())
  }

  single<BarrierTokenDataSource> {
    BarrierTokenDataSource(get())
  }

  single<AuthenticationRepository> {
    DefaultAuthenticationRepository(get(), get())
  }

  single<ChatRepository> {
    DefaultChatRepository(get(), get(), get(), get())
  }

  single<FriendRepository> {
    DefaultFriendRepository(get())
  }

  single<MessageRepository> {
    DefaultMessageRepository(get(), get(), get(), get(), get())
  }

  single<ProfileRepository> {
    DefaultProfileRepository(get(), get(), get())
  }

  single<SearchRepository> {
    DefaultSearchRepository(get())
  }
  single<MessageAsyncRepository> {
    DefaultMessageAsyncRepository()
  }

//  single<RegistrationRepository> {
//    DefaultRegistrationRepository(get(), get(), get())
//  }

}