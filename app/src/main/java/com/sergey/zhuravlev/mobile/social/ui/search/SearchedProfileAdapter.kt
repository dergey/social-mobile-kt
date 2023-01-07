package com.sergey.zhuravlev.mobile.social.ui.search

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
import com.sergey.zhuravlev.mobile.social.ui.common.GlidePlaceholder
import com.sergey.zhuravlev.mobile.social.ui.common.ModelComparators
import com.sergey.zhuravlev.mobile.social.ui.profile.ProfileActivity

class SearchedProfileAdapter(private val context: Context) :
  PagingDataAdapter<ProfileModel, RecyclerView.ViewHolder>(ModelComparators.PROFILE_COMPARATOR) {

  val pagingDataCount: Int
    get() = super.getItemCount()

  override fun getItemViewType(position: Int): Int {
    return getItem(position)?.let { DEFAULT_VIEW_TYPE } ?: PLACEHOLDER_VIEW_TYPE
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      DEFAULT_VIEW_TYPE -> SearchedProfileViewHolder.getInstance(parent, context)
      PLACEHOLDER_VIEW_TYPE -> Placeholder.getInstance(parent)
      else -> throw IllegalArgumentException("Unknown viewType = $viewType")
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    getItem(position)?.let { item ->
      if (holder is SearchedProfileViewHolder) {
        holder.bind(item)
      }
    }
  }

  override fun getItemCount(): Int {
    val itemCount = super.getItemCount()
    return if (itemCount > 3) 3 else itemCount
  }

  internal class SearchedProfileViewHolder(val context: Context, itemView: View)
    : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
      itemView.setOnClickListener(this)
    }

    private val avatarImageView: ImageView = itemView.findViewById(R.id.avatar_image_view)
    private val fullNameTextView: TextView = itemView.findViewById(R.id.full_name_text_view)
    private val actionImageView: ImageView = itemView.findViewById(R.id.action_image_view)
    private val moreActionImageView: ImageView = itemView.findViewById(R.id.more_image_view)

    private lateinit var item: ProfileModel

    fun bind(item: ProfileModel): SearchedProfileViewHolder {
      this.item = item

      fullNameTextView.text = context.getString(
        R.string.profile_item_short_name,
        item.firstName, item.secondName
      )

      actionImageView.visibility = when (item.attitude) {
        "FRIEND_INCOMING", "FRIEND_OUTGOING", "FRIEND", "NONE" -> View.VISIBLE
        else -> View.GONE
      }

      actionImageView.setImageResource(
        when (item.attitude) {
          "FRIEND_OUTGOING" -> R.drawable.ic_round_person_add_disabled_24
          "FRIEND" -> R.drawable.ic_round_chat_bubble_outline_24
          else -> R.drawable.ic_round_person_add_24
        }
      )

      Glide.with(context)
        .load(item.avatarLink)
        .placeholder(GlidePlaceholder.instance)
        .error(ContextCompat.getDrawable(context, R.drawable.ic_round_no_accounts_24))
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .circleCrop()
        .into(avatarImageView)

      actionImageView.setOnClickListener {
        //todo implement
      }

      moreActionImageView.setOnClickListener {
        //todo implement
      }

      return this
    }

    override fun onClick(v: View) {
      ProfileActivity.open(context, item.username)
    }

    companion object {
      fun getInstance(parent: ViewGroup, context: Context): SearchedProfileViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_searched_profile, parent, false)
        return SearchedProfileViewHolder(context, view)
      }
    }
  }

  internal class Placeholder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
      fun getInstance(parent: ViewGroup): Placeholder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.placeholder_searched_profile, parent, false)
        return Placeholder(view)
      }
    }
  }

  companion object {
    private const val PLACEHOLDER_VIEW_TYPE: Int = 0
    private const val DEFAULT_VIEW_TYPE: Int = 1
  }
}