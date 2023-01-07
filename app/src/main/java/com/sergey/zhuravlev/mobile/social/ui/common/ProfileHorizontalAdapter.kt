package com.sergey.zhuravlev.mobile.social.ui.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sergey.zhuravlev.mobile.social.R
import com.sergey.zhuravlev.mobile.social.domain.model.ProfileModel
import com.sergey.zhuravlev.mobile.social.ui.profile.ProfileActivity

class ProfileHorizontalAdapter(private val context: Context) :
  PagingDataAdapter<ProfileModel, RecyclerView.ViewHolder>(ModelComparators.PROFILE_COMPARATOR) {

  override fun getItemViewType(position: Int): Int {
    return getItem(position)?.let { DEFAULT_VIEW_TYPE } ?: PLACEHOLDER_VIEW_TYPE
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      DEFAULT_VIEW_TYPE -> ProfileHorizontalViewHolder.getInstance(parent)
      PLACEHOLDER_VIEW_TYPE -> Placeholder.getInstance(parent)
      else -> throw IllegalArgumentException("Unknown viewType = $viewType")
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    getItem(position)?.let { item ->
      if (holder is ProfileHorizontalViewHolder) {
        holder.bind(item, context)
      }
    }
  }

  internal class ProfileHorizontalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    init {
      itemView.setOnClickListener(this)
    }

    private val avatarImageView: ImageView = itemView.findViewById(R.id.avatar_image_view)
    private val fullNameTextView: TextView = itemView.findViewById(R.id.full_name_text_view)

    private lateinit var item: ProfileModel
    private lateinit var context: Context

    fun bind(item: ProfileModel, context: Context): ProfileHorizontalViewHolder {
      this.item = item
      this.context = context

      fullNameTextView.text = context.getString(
        R.string.profile_horizontal_item_short_name,
        item.firstName, item.secondName
      )

      Glide.with(context)
        .load(item.avatarLink)
        .placeholder(GlidePlaceholder.instance)
        .error(ContextCompat.getDrawable(context, R.drawable.ic_round_no_accounts_24))
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .circleCrop()
        .into(avatarImageView)

      return this
    }

    override fun onClick(v: View) {
      ProfileActivity.open(context, item.username)
    }

    companion object {
      fun getInstance(parent: ViewGroup): ProfileHorizontalViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_circle_profile, parent, false)
        return ProfileHorizontalViewHolder(view)
      }
    }
  }

  internal class Placeholder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
      fun getInstance(parent: ViewGroup): Placeholder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.placeholder_circle_profile, parent, false)
        return Placeholder(view)
      }
    }
  }

  companion object {
    private const val PLACEHOLDER_VIEW_TYPE: Int = 0
    private const val DEFAULT_VIEW_TYPE: Int = 1
  }
}