package com.sergey.zhuravlev.mobile.social.data.mapper.transform

import com.sergey.zhuravlev.mobile.social.data.api.SocialApi
import com.sergey.zhuravlev.mobile.social.data.database.entity.*
import com.sergey.zhuravlev.mobile.social.data.utils.LinkEnchanter
import com.sergey.zhuravlev.mobile.social.domain.enums.RelationshipStatus
import com.sergey.zhuravlev.mobile.social.domain.model.ChatModel
import com.sergey.zhuravlev.mobile.social.domain.model.MessageModel
import com.sergey.zhuravlev.mobile.social.domain.model.ProfileModel

fun ChatAndLastMessageEntities.toDomainModel(): ChatModel {
  return ChatModel(
    networkId = this.chat.id,
    targetProfile = this.chat.targetProfile.toDomainModel(),
    createAt = this.chat.createAt,
    updateAt = this.chat.updateAt,
    messageAllow = this.chat.messageAllow,
    unreadMessages = this.chat.unreadMessages ?: 0L,
    lastMessage = this.lastMessage.toDomainModel()
  )
}

fun ChatEntity.toDomainModel(): ChatModel {
  return ChatModel(
    networkId = this.id,
    targetProfile = this.targetProfile.toDomainModel(),
    createAt = this.createAt,
    updateAt = this.updateAt,
    messageAllow = this.messageAllow,
    unreadMessages = this.unreadMessages ?: 0L,
  )
}

fun MessageEntity.toDomainModel(): MessageModel {
  return MessageModel(
    id = this.id,
    networkId = this.networkId,
    chatNetworkId = this.chatId,
    type = this.type,
    sender = this.sender,
    createAt = this.createAt,
    updateAt = this.updateAt,
    read = this.read,
    text = this.text,
    imageLink = if (this.type == "IMAGE")
      LinkEnchanter.enchant(
        SocialApi.MESSAGE_IMAGE_PATTERN,
        SocialApi.BASE_URL,
        this.chatId,
        this.networkId
      ) else null,
    glideSignature = this.glideSignature,
    prepend = this.prepend,
    hasPrependError = this.hasPrependError
  )
}

fun ProfilePreviewEmbeddable.toDomainModel(): ProfileModel {
  return ProfileModel(
    username = this.username,
    firstName = this.firstName,
    middleName = this.middleName,
    secondName = this.secondName,
    gender = this.gender,
    city = this.city,
    birthDate = this.birthDate,
    attitude = this.attitude,
    avatarLink = LinkEnchanter.enchant(
      SocialApi.PROFILE_AVATAR_PATTERN,
      SocialApi.BASE_URL,
      this.username
    )
  )
}

fun ProfileAndDetailEntities.toDomainModel(): ProfileModel {
  return ProfileModel(
    username = this.profile.username,
    firstName = this.profile.firstName,
    middleName = this.profile.middleName,
    secondName = this.profile.secondName,
    gender = this.profile.gender,
    city = this.profile.city,
    birthDate = this.profile.birthDate,
    overview = this.detail.overview,
    relationshipStatus = this.detail.relationshipStatus?.let { RelationshipStatus.valueOf(it) },
    workplace = this.detail.workplace,
    education = this.detail.education,
    citizenship = this.detail.citizenship,
    registrationAddress = this.detail.registrationAddress,
    residenceAddress = this.detail.residenceAddress,
    createAt = this.detail.createAt,
    updateAt = this.detail.updateAt,
    attitude = this.profile.attitude,
    avatarLink = LinkEnchanter.enchant(
      SocialApi.PROFILE_AVATAR_PATTERN,
      SocialApi.BASE_URL,
      this.profile.username
    )
  )
}