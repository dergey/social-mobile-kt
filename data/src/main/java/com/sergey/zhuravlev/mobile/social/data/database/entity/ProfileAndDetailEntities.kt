package com.sergey.zhuravlev.mobile.social.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

class ProfileAndDetailEntities(
  @Embedded
  val profile: ProfileEntity,
  @Relation(parentColumn = "username", entityColumn = "username")
  val detail: ProfileDetailEntity
)