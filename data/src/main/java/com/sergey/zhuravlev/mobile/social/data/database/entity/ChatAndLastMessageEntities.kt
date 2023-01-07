package com.sergey.zhuravlev.mobile.social.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

class ChatAndLastMessageEntities(
  @Embedded
  val chat: ChatEntity,
  @Relation(parentColumn = "last_message_id", entityColumn = "id")
  val lastMessage: MessageEntity
)