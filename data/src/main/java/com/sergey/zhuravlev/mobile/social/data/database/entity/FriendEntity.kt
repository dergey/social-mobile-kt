package com.sergey.zhuravlev.mobile.social.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friends")
class FriendEntity(
  @PrimaryKey
  val username: String
)