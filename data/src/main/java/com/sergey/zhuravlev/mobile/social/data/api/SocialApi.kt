package com.sergey.zhuravlev.mobile.social.data.api

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.sergey.zhuravlev.mobile.social.data.api.factory.ResponseCallAdapterFactory
import com.sergey.zhuravlev.mobile.social.data.api.service.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


object SocialApi {

  const val BASE_URL = "https://social.xpolr.space"

  const val PROFILE_AVATAR_PATTERN = "%s/api/profile/%s/avatar"
  const val MESSAGE_IMAGE_PATTERN = "%s/api/chat/%s/message/%s/image"

  private val bearerTokenInterceptor = BearerTokenInterceptor()
  private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
    setLevel(HttpLoggingInterceptor.Level.BODY)
  }

  private val client = OkHttpClient.Builder()
    .addInterceptor(bearerTokenInterceptor)
    .addInterceptor(httpLoggingInterceptor)
    .followRedirects(false)
    .build()

  private val objectMapper = ObjectMapper()

  private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(JacksonConverterFactory.create(objectMapper))
    .addCallAdapterFactory(ResponseCallAdapterFactory())
    .client(client)
    .build()

  val authenticationService: AuthenticationService by lazy {
    retrofit.create(
      AuthenticationService::class.java
    )
  }
  val chatService: ChatService by lazy { retrofit.create(ChatService::class.java) }
  val friendService: FriendService by lazy { retrofit.create(FriendService::class.java) }
  val messageService: MessageService by lazy { retrofit.create(MessageService::class.java) }
  val passwordResetService: PasswordResetService by lazy {
    retrofit.create(
      PasswordResetService::class.java
    )
  }
  val profilePropertiesService: ProfilePropertiesService by lazy {
    retrofit.create(
      ProfilePropertiesService::class.java
    )
  }
  val profileService: ProfileService by lazy { retrofit.create(ProfileService::class.java) }
  val registrationService: RegistrationService by lazy {
    retrofit.create(
      RegistrationService::class.java
    )
  }
  val searchService: SearchService by lazy { retrofit.create(SearchService::class.java) }
  val stickerService: StickerService by lazy { retrofit.create(StickerService::class.java) }
  val userService: UserService by lazy { retrofit.create(UserService::class.java) }

  fun setBearerToken(bearerToken: String) {
    bearerTokenInterceptor.barrierToken = bearerToken
  }

  fun getBearerToken(): String? {
    return bearerTokenInterceptor.barrierToken
  }

  init {
    objectMapper
      .registerModule(JavaTimeModule())
      .registerModule(JsonMapperDateTimeModule)
      .registerModule(
        KotlinModule.Builder()
          .withReflectionCacheSize(512)
          .configure(KotlinFeature.NullToEmptyCollection, false)
          .configure(KotlinFeature.NullToEmptyMap, false)
          .configure(KotlinFeature.NullIsSameAsDefault, false)
          .configure(KotlinFeature.SingletonSupport, false)
          .configure(KotlinFeature.StrictNullChecks, false)
          .build()
      )
  }

  private object JsonMapperDateTimeModule : SimpleModule() {

    private val DEFAULT_ZONE_ID = ZoneId.systemDefault()
    private val UTC_ZONE_ID = ZoneId.of("UTC")

    init {
      addDeserializer(ZonedDateTime::class.java, object : JsonDeserializer<ZonedDateTime>() {
        @Throws(IOException::class, JsonProcessingException::class)
        override fun deserialize(
          jsonParser: JsonParser,
          deserializationContext: DeserializationContext
        ): ZonedDateTime? {
          return ZonedDateTime.parse(
            jsonParser.valueAsString,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
          )
            .withZoneSameInstant(DEFAULT_ZONE_ID)
        }
      })
      addDeserializer(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime>() {
        @Throws(IOException::class, JsonProcessingException::class)
        override fun deserialize(
          jsonParser: JsonParser,
          deserializationContext: DeserializationContext
        ): LocalDateTime? {
          return ZonedDateTime.parse(
            jsonParser.valueAsString,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
          )
            .withZoneSameInstant(DEFAULT_ZONE_ID)
            .toLocalDateTime()
        }
      })
      addSerializer(ZonedDateTime::class.java, object : JsonSerializer<ZonedDateTime>() {
        @Throws(IOException::class)
        override fun serialize(
          zonedDateTime: ZonedDateTime,
          jsonGenerator: JsonGenerator,
          serializerProvider: SerializerProvider
        ) {
          jsonGenerator.writeString(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(
            zonedDateTime.withZoneSameInstant(UTC_ZONE_ID))
          )
        }
      })
      addSerializer(LocalDateTime::class.java, object : JsonSerializer<LocalDateTime>() {
        @Throws(IOException::class)
        override fun serialize(
          localDateTime: LocalDateTime,
          jsonGenerator: JsonGenerator,
          serializerProvider: SerializerProvider
        ) {
          jsonGenerator.writeString(
            DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(
              localDateTime.atZone(
                DEFAULT_ZONE_ID
              )
            )
          )
        }
      })
    }
  }
}
