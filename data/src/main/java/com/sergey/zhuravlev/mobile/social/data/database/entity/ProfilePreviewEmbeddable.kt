package com.sergey.zhuravlev.mobile.social.data.database.entity

import androidx.room.ColumnInfo
import java.time.LocalDate

class ProfilePreviewEmbeddable(
  val username: String,
  @ColumnInfo(name = "first_name")
  val firstName: String,
  @ColumnInfo(name = "middle_name")
  val middleName: String? = null,
  @ColumnInfo(name = "second_name")
  val secondName: String,
  val gender: String? = null,
  val city: String? = null,
  @ColumnInfo(name = "birth_date")
  val birthDate: LocalDate? = null,
  val attitude: String
)