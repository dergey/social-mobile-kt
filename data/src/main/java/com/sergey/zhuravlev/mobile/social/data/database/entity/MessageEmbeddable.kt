package com.sergey.zhuravlev.mobile.social.data.database.entity

import androidx.room.ColumnInfo
import java.time.LocalDateTime

class MessageEmbeddable(
  @ColumnInfo(name = "network_id")
  val networkId: Long,
  val type: String,
  val sender: String,
  @ColumnInfo(name = "create_at")
  val createAt: LocalDateTime,
  @ColumnInfo(name = "update_at")
  val updateAt: LocalDateTime,
  val read: Boolean,
  val text: String? = null,
)