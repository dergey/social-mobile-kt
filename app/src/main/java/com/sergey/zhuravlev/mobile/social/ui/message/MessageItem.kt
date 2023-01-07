package com.sergey.zhuravlev.mobile.social.ui.message

import com.sergey.zhuravlev.mobile.social.domain.model.MessageModel
import java.time.LocalDate

open class MessageItem {
  data class RepoItem(val model: MessageModel) : MessageItem()
  data class DateSeparatorItem(val date: LocalDate) : MessageItem()
}