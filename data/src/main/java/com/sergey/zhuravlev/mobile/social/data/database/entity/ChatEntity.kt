package com.sergey.zhuravlev.mobile.social.data.database.entity


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sergey.zhuravlev.mobile.social.data.database.pageable.Pageable

import java.time.LocalDateTime

@Entity(tableName = "chats")
class ChatEntity(
  @PrimaryKey
  val id: Long,
  @Embedded(prefix = "profile_")
  var targetProfile: ProfilePreviewEmbeddable,
  @ColumnInfo(name = "create_at")
  var createAt: LocalDateTime,
  @ColumnInfo(name = "update_at")
  var updateAt: LocalDateTime,
  @ColumnInfo(name = "message_allow")
  var messageAllow: Boolean,
  @ColumnInfo(name = "unread_messages")
  var unreadMessages: Long? = null,
  @ColumnInfo(name = "last_message_id")
  var lastMessageId: Long? = null,
  @Embedded(prefix = "pageable_")
  var pageable: Pageable? = null,
)