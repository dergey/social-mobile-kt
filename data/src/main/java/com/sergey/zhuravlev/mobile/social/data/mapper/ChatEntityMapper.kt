package com.sergey.zhuravlev.mobile.social.data.mapper

import com.sergey.zhuravlev.mobile.social.data.api.dto.ChatDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.ChatPreviewDto
import com.sergey.zhuravlev.mobile.social.data.database.entity.ChatEntity
import com.sergey.zhuravlev.mobile.social.data.database.entity.MessageEntity

object ChatEntityMapper {

  fun toEntity(dto: ChatPreviewDto, messageEntity: MessageEntity): ChatEntity {
    return ChatEntity(
      id = dto.id,
      targetProfile = ProfileEntityMapper.toEmbeddable(dto.targetProfile),
      createAt = dto.createAt,
      updateAt = dto.updateAt,
      messageAllow = dto.messageAllow,
      unreadMessages = dto.unreadMessages,
      lastMessageId = messageEntity.id
    )
  }

  fun toEntity(dto: ChatDto, lastMessageEntity: MessageEntity): ChatEntity {
    return ChatEntity(
      id = dto.id,
      targetProfile = ProfileEntityMapper.toEmbeddable(dto.targetProfile),
      createAt = dto.createAt,
      updateAt = dto.updateAt,
      messageAllow = dto.messageAllow,
      lastMessageId = lastMessageEntity.id
    )
  }

  fun toEntity(dto: ChatDto): ChatEntity {
    return ChatEntity(
      id = dto.id,
      targetProfile = ProfileEntityMapper.toEmbeddable(dto.targetProfile),
      createAt = dto.createAt,
      updateAt = dto.updateAt,
      messageAllow = dto.messageAllow
    )
  }

  fun updateEntity(entity: ChatEntity, dto: ChatPreviewDto, lastMessageEntity: MessageEntity): ChatEntity {
    return ChatEntity(
      id = entity.id,
      targetProfile = ProfileEntityMapper.toEmbeddable(dto.targetProfile),
      createAt = dto.createAt,
      updateAt = dto.updateAt,
      messageAllow = dto.messageAllow,
      unreadMessages = dto.unreadMessages,
      lastMessageId = lastMessageEntity.id,
      pageable = entity.pageable
    )
  }

  fun updateEntity(entity: ChatEntity, dto: ChatDto, messageEntity: MessageEntity): ChatEntity {
    return ChatEntity(
      id = entity.id,
      targetProfile = ProfileEntityMapper.toEmbeddable(dto.targetProfile),
      createAt = dto.createAt,
      updateAt = dto.updateAt,
      messageAllow = dto.messageAllow,
      unreadMessages = entity.unreadMessages,
      lastMessageId = messageEntity.id,
      pageable = entity.pageable
    )
  }
}