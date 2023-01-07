package com.sergey.zhuravlev.mobile.social.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sergey.zhuravlev.mobile.social.data.database.pageable.Pageable
import java.time.LocalDateTime

@Entity(tableName = "messages")
class MessageEntity(
  @PrimaryKey(autoGenerate = true)
  var id: Long? = null,
  @ColumnInfo(name = "network_id")
  var networkId: Long? = null,
  @ColumnInfo(name = "chat_id")
  var chatId: Long,
  var type: String,
  var sender: String,
  @ColumnInfo(name = "create_at")
  var createAt: LocalDateTime,
  @ColumnInfo(name = "update_at")
  var updateAt: LocalDateTime,
  var read: Boolean,
  var text: String? = null,
  @ColumnInfo(name = "glide_signature")
  var glideSignature: String? = null,
  var prepend: Boolean = false,
  @ColumnInfo(name = "prepend_error")
  var hasPrependError: Boolean = false,
  @Embedded(prefix = "pageable_")
  var pageable: Pageable? = null
)