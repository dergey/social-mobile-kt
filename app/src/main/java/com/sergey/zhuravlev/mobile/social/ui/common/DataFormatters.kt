package com.sergey.zhuravlev.mobile.social.ui.common

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DataFormatters {

  private val TIME_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)

  private val DATE_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)

  private val FULLY_DATE_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH)


  fun LocalDate.toShortFormattedString(): String {
    return this.format(DATE_FORMATTER)
  }

  fun LocalDate.toFullyFormattedString(): String {
    return this.format(FULLY_DATE_FORMATTER)
  }

  fun LocalDateTime.toShortDateFormattedString(): String {
    return this.format(DATE_FORMATTER)
  }

  fun LocalDateTime.toFullyDateFormattedString(): String {
    return this.format(FULLY_DATE_FORMATTER)
  }

  fun LocalDateTime.toTimeFormattedString(): String {
    return this.format(TIME_FORMATTER)
  }
}