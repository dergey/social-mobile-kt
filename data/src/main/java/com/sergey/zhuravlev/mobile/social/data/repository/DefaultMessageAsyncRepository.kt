package com.sergey.zhuravlev.mobile.social.data.repository

import com.sergey.zhuravlev.mobile.social.domain.repository.MessageAsyncRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import java.time.Duration

class DefaultMessageAsyncRepository : MessageAsyncRepository {

  private val client = StompClient(
    OkHttpWebSocketClient(
      client = OkHttpClient.Builder()
        .callTimeout(Duration.ofMinutes(1))
        .pingInterval(Duration.ofSeconds(10))
        .build())
  )
  private var session: StompSession? = null

  override suspend fun subscribe(): Flow<String> {
    session =
      client.connect("wss://social.xpolr.space/websocket/android-messages")
    return session!!.subscribeText("/topic/messages")
  }

  override suspend fun unsubscribe() {
    session?.disconnect()
  }
}