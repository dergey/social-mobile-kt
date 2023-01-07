package com.sergey.zhuravlev.mobile.social.data.mapper

import com.sergey.zhuravlev.mobile.social.data.api.dto.message.MessageDto
import com.sergey.zhuravlev.mobile.social.data.api.dto.message.TextMessageDto
import com.sergey.zhuravlev.mobile.social.data.database.entity.MessageEmbeddable
import com.sergey.zhuravlev.mobile.social.data.database.entity.MessageEntity

object MessageEntityMapper {

  fun toEmbeddable(dto: MessageDto): MessageEmbeddable {
    return MessageEmbeddable(
      networkId = dto.id,
      type = dto.type.name,
      sender = dto.sender.name,
      createAt = dto.createAt,
      updateAt = dto.updateAt,
      read = dto.read,
      text = if (dto is TextMessageDto) dto.text else null
    )
  }

  fun toEntity(dto: MessageDto, chatId: Long): MessageEntity {
    return MessageEntity(
      networkId = dto.id,
      chatId = chatId,
      type = dto.type.name,
      sender = dto.sender.name,
      createAt = dto.createAt,
      updateAt = dto.updateAt,
      read = dto.read,
      text = if (dto is TextMessageDto) dto.text else null
    )
  }

  fun updateEntity(model: MessageEntity, dto: MessageDto): MessageEntity {
    return MessageEntity(
      id = model.id,
      networkId = dto.id,
      chatId = model.chatId,
      type = dto.type.name,
      sender = dto.sender.name,
      createAt = dto.createAt,
      updateAt = dto.updateAt,
      read = dto.read,
      text = if (dto is TextMessageDto) dto.text else null,
      glideSignature = model.glideSignature,
      prepend = false,
      hasPrependError = false,
      pageable = model.pageable
    )
  }

}