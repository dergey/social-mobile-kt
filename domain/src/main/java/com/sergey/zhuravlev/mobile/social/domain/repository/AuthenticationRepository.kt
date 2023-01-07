package com.sergey.zhuravlev.mobile.social.domain.repository

import com.sergey.zhuravlev.mobile.social.domain.model.Operation

interface AuthenticationRepository {

  suspend fun authenticate(email: String, password: String): Operation<Void>
}
