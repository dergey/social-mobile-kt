package com.sergey.zhuravlev.mobile.social.domain.repository

import kotlinx.coroutines.flow.Flow

interface MessageAsyncRepository {

  suspend fun subscribe(): Flow<String>

  suspend fun unsubscribe()
}