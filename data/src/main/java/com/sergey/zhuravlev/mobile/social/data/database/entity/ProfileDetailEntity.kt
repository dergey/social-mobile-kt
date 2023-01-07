package com.sergey.zhuravlev.mobile.social.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "profile_details")
class ProfileDetailEntity(
  @PrimaryKey
  val username: String,
  val overview: String? = null,
  @ColumnInfo(name = "relationship_status")
  val relationshipStatus: String? = null,
  val workplace: String? = null,
  val education: String? = null,
  val citizenship: String? = null,
  val registrationAddress: String? = null,
  val residenceAddress: String? = null,
  @ColumnInfo(name = "create_at")
  val createAt: LocalDateTime,
  @ColumnInfo(name = "update_at")
  val updateAt: LocalDateTime
)