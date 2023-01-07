package com.sergey.zhuravlev.mobile.social.data.api.dto.confirmation

import java.util.UUID

data class ManualCodeConfirmationDto(
  val continuationCode: UUID,
  val manualCode: String
)