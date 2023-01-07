package com.sergey.zhuravlev.mobile.social.ui.message

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.signature.ObjectKey
import com.sergey.zhuravlev.mobile.social.R
import com.sergey.zhuravlev.mobile.social.data.api.SocialApi
import com.sergey.zhuravlev.mobile.social.domain.model.MessageModel
import com.sergey.zhuravlev.mobile.social.ui.common.DataFormatters.toFullyFormattedString
import com.sergey.zhuravlev.mobile.social.ui.common.DataFormatters.toTimeFormattedString
import java.time.LocalDate

class MessageAdapter(
  val context: Context
) : PagingDataAdapter<MessageItem, RecyclerView.ViewHolder>(REPOSITORY_COMPARATOR) {

  override fun getItemViewType(position: Int): Int {
    return getItem(position)?.let { item ->
      when (item) {
        is MessageItem.RepoItem ->
          when (item.model.type) {
            "TEXT", "SERVICE" -> TEXT_MESSAGE_VIEW_TYPE
            "IMAGE" -> IMAGE_MESSAGE_VIEW_TYPE
            else ->
              throw IllegalArgumentException("Not supported message.type = ${item.model.type}")
          }
        is MessageItem.DateSeparatorItem -> SEPARATOR_VIEW_TYPE
        else ->
          throw IllegalArgumentException("Not supported item = ${item.javaClass.simpleName}")
      }
    } ?: PLACEHOLDER_VIEW_TYPE
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      TEXT_MESSAGE_VIEW_TYPE -> MessageViewHolder.getInstance(
        parent,
        context
      )
      IMAGE_MESSAGE_VIEW_TYPE -> MessageImageViewHolder.getInstance(
        parent,
        context
      )
      SEPARATOR_VIEW_TYPE -> Separator.getInstance(parent)
      PLACEHOLDER_VIEW_TYPE -> Placeholder.getInstance(parent)
      else -> throw IllegalArgumentException("Unknown viewType = $viewType")
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    getItem(position)?.let { item ->
      when (holder) {
        is MessageViewHolder -> holder.bind((item as MessageItem.RepoItem).model)
        is MessageImageViewHolder -> holder.bind((item as MessageItem.RepoItem).model)
        is Separator -> holder.bind((item as MessageItem.DateSeparatorItem).date)
        else -> null
      }
    }
  }

  internal class MessageViewHolder(val context: Context, itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val constraintLayout = itemView.rootView as ConstraintLayout

    private val messageText: TextView = itemView.findViewById(R.id.message_text_view)
    private val messageDate: TextView = itemView.findViewById(R.id.message_date_text_view)
    private val sendErrorImageView: ImageView = itemView.findViewById(R.id.send_error_image_view)
    private val sendStatusImageView: ImageView = itemView.findViewById(R.id.send_status_image_view)

    fun bind(item: MessageModel): MessageViewHolder {

      if (item.sender == "TARGET") {
        constraintLayout.moveLeft(R.id.card_view)
        sendStatusImageView.visibility = View.GONE
      } else {
        constraintLayout.moveRight(R.id.card_view)
        sendStatusImageView.visibility = View.VISIBLE
      }

      when (item.type) {
        "TEXT", "SERVICE" -> messageText.text = item.text
        "IMAGE" -> messageText.setText(R.string.message_image_text)
        "STICKER" -> messageText.setText(R.string.message_sticker_text)
      }

      messageDate.text = item.createAt.toTimeFormattedString()

      // Status logic:
      sendStatusImageView.setImageResource(
        if (item.prepend) {
          R.drawable.ic_round_access_time_24
        } else {
          if (item.read) {
            R.drawable.ic_round_done_all_24
          } else {
            R.drawable.ic_round_done_24
          }
        }
      )

      // Error logic:
      sendErrorImageView.visibility = if (item.hasPrependError)
        View.VISIBLE
      else
        View.GONE

      return this
    }

    companion object {
      fun getInstance(
        parent: ViewGroup,
        context: Context
      ): MessageViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(context, view)
      }
    }
  }

  internal class MessageImageViewHolder(val context: Context, itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val constraintLayout = itemView.rootView as ConstraintLayout

    private val messageImage: ImageView = itemView.findViewById(R.id.message_image_view)
    private val messageDate: TextView = itemView.findViewById(R.id.message_date_text_view)

    fun bind(item: MessageModel): MessageImageViewHolder {

      if (item.sender == "TARGET") {
        constraintLayout.moveLeft(R.id.image_layout)
      } else {
        constraintLayout.moveRight(R.id.image_layout)
      }

      messageDate.text = item.createAt.toTimeFormattedString()

      if (!item.prepend) {
        val glideUrl = GlideUrl(
          item.imageLink,
          LazyHeaders.Builder()
            .addHeader("Authorization", "Bearer " + SocialApi.getBearerToken())
            .build()
        )
        Glide.with(context)
          .load(glideUrl)
          .signature(ObjectKey(item.imageLink!!))
          .fitCenter()
          .into(messageImage)
      } else {
        val uri = Uri.parse(item.glideSignature)
        val objectKey = ObjectKey(item.glideSignature!!)
        Glide.with(context).asBitmap()
          .load(uri)
          .signature(objectKey)
          .onlyRetrieveFromCache(true)
          .into(messageImage)
      }
      return this
    }

    companion object {
      fun getInstance(
        parent: ViewGroup,
        context: Context
      ): MessageImageViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_message_image, parent, false)
        return MessageImageViewHolder(context, view)
      }
    }
  }

  internal class Separator(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val dateTextView: TextView = itemView.findViewById(R.id.date_text_view)

    fun bind(date: LocalDate): Separator {
      dateTextView.text = date.toFullyFormattedString()
      return this
    }

    companion object {
      fun getInstance(parent: ViewGroup): Separator {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_separator_message, parent, false)
        return Separator(view)
      }
    }
  }

  internal class Placeholder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
      fun getInstance(parent: ViewGroup): Placeholder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.placeholder_message, parent, false)
        return Placeholder(view)
      }
    }
  }

  companion object {
    private const val TEXT_MESSAGE_VIEW_TYPE = 0
    private const val IMAGE_MESSAGE_VIEW_TYPE = 1
    private const val SEPARATOR_VIEW_TYPE = 100
    private const val PLACEHOLDER_VIEW_TYPE = 200

    private val REPOSITORY_COMPARATOR = object : DiffUtil.ItemCallback<MessageItem>() {

      override fun areItemsTheSame(
        oldItem: MessageItem,
        newItem: MessageItem
      ): Boolean {
        val isSameChatPreviewModel = (
            oldItem is MessageItem.RepoItem
                && newItem is MessageItem.RepoItem
                && oldItem.model.id == newItem.model.id
            )
        val isSameSeparator = (
            oldItem is MessageItem.DateSeparatorItem
                && newItem is MessageItem.DateSeparatorItem
                && oldItem.date == newItem.date
            )
        return isSameChatPreviewModel || isSameSeparator
      }

      override fun areContentsTheSame(
        oldItem: MessageItem,
        newItem: MessageItem
      ): Boolean {
        val isSameChatPreviewModel = (
            oldItem is MessageItem.RepoItem
                && newItem is MessageItem.RepoItem
                && oldItem.model == newItem.model
            )
        val isSameSeparator = (
            oldItem is MessageItem.DateSeparatorItem
                && newItem is MessageItem.DateSeparatorItem
                && oldItem.date == newItem.date
            )
        return isSameChatPreviewModel || isSameSeparator
      }
    }

    fun ConstraintLayout.moveRight(viewId: Int) {
      ConstraintSet().apply {
        clone(this@moveRight)
        setHorizontalBias(viewId, 1.0f)
        applyTo(this@moveRight)
      }
    }

    fun ConstraintLayout.moveLeft(viewId: Int) {
      ConstraintSet().apply {
        clone(this@moveLeft)
        setHorizontalBias(viewId, 0.0f)
        applyTo(this@moveLeft)
      }
    }
  }
}



