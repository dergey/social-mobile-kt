package com.sergey.zhuravlev.mobile.social.data.database.converter

import androidx.room.TypeConverter
import java.time.*

class LocalDateTimeConverter {

  @TypeConverter
  fun toEpochMilliConvert(localDateTime: LocalDateTime?): Long? {
    return localDateTime?.let { toEpochMilli(it) }
  }

  @TypeConverter
  fun fromEpochMilliConvert(epochMilli: Long?): LocalDateTime? {
    return epochMilli?.let { fromEpochMilli(it) }
  }

  @TypeConverter
  fun toEpochDayConvert(localDate: LocalDate?): Long? {
    return localDate?.let { toEpochDay(it) }
  }

  @TypeConverter
  fun fromEpochDayConvert(epochDay: Long?): LocalDate? {
    return epochDay?.let { fromEpochDay(it) }
  }

  companion object {
    private val DEFAULT_ZONE_ID = ZoneId.of("GMT")

    fun toEpochMilli(localDateTime: LocalDateTime): Long {
      return ZonedDateTime.of(localDateTime, DEFAULT_ZONE_ID)
          .toInstant().toEpochMilli()
    }

    fun fromEpochMilli(epochMilli: Long): LocalDateTime {
      return Instant.ofEpochMilli(epochMilli)
          .atZone(DEFAULT_ZONE_ID).toLocalDateTime()
    }

    fun toEpochDay(localDate: LocalDate): Long {
      return localDate.toEpochDay()
    }

    fun fromEpochDay(epochDay: Long): LocalDate {
      return LocalDate.ofEpochDay(epochDay)
    }
  }
}