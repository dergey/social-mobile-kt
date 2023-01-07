package com.sergey.zhuravlev.mobile.social.data.repository

import com.sergey.zhuravlev.mobile.social.data.api.SocialApi
import com.sergey.zhuravlev.mobile.social.data.api.dto.LoginDto
import com.sergey.zhuravlev.mobile.social.data.api.service.AuthenticationService
import com.sergey.zhuravlev.mobile.social.data.datasource.BarrierTokenDataSource
import com.sergey.zhuravlev.mobile.social.data.mapper.transform.toOperation
import com.sergey.zhuravlev.mobile.social.domain.model.Operation
import com.sergey.zhuravlev.mobile.social.domain.repository.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultAuthenticationRepository(
  private val authenticationService: AuthenticationService,
  private val barrierTokenDataSource: BarrierTokenDataSource
) : AuthenticationRepository {

  override suspend fun authenticate(email: String, password: String): Operation<Void> {
    return withContext(Dispatchers.IO) {
      val response = authenticationService.authenticate(LoginDto(email, password, true))

      response.onSuccess {
        SocialApi.setBearerToken(it.jwtToken)
        barrierTokenDataSource.saveBarrierToken(it.jwtToken)
      }

      return@withContext response.toOperation()
    }
  }
}