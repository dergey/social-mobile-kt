package com.sergey.zhuravlev.mobile.social.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sergey.zhuravlev.mobile.social.R
import com.sergey.zhuravlev.mobile.social.databinding.ActivityProfileBinding
import com.sergey.zhuravlev.mobile.social.ui.common.DataFormatters.toFullyFormattedString
import com.sergey.zhuravlev.mobile.social.ui.common.DataFormatters.toShortDateFormattedString
import com.sergey.zhuravlev.mobile.social.ui.common.DataFormatters.toTimeFormattedString
import com.sergey.zhuravlev.mobile.social.ui.common.GlidePlaceholder
import com.sergey.zhuravlev.mobile.social.ui.common.ProfileHorizontalAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val vm: ProfileViewModel by viewModel()
    vm.parseIntent(intent)

    val binding = ActivityProfileBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // Header initializing:
    val backImageView = binding.backImageView
    val menuImageView = binding.menuImageView

    val constraintLayoutMain = binding.constraintLayoutMain
    val fullNameTextView = binding.fullnameTextView
    val statusTextView = binding.statusTextView
    val cityTextView = binding.cityTextView
    val joinedTextView = binding.joinedTextView
    val avatarImageView = binding.avatarImageView
    val friendStatusImageView = binding.friendStatusImageView


    val overviewTextView = binding.overviewTextView
    val overviewSubcardLayout = binding.overviewSubcardLayout
    val bornValueTextView = binding.bornValueTextView
    val bornSubcardLayout = binding.bornSubcardLayout
    val statusValueTextView = binding.statusValueTextView
    val statusSubcardLayout = binding.statusSubcardLayout

    backImageView.setOnClickListener { this@ProfileActivity.onBackPressed() }

//    menuImageView.setOnClickListener {
//      val popup = PopupMenu(this@ProfileActivity, it)
//      popup.setOnMenuItemClickListener(onMenuItemClickListener)
//      val inflater = popup.menuInflater
//      inflater.inflate(R.menu.profile_menu, popup.menu)
//      popup.show()
//    }

    vm.profile.observe(this) { profile ->
      // Setting values for the top header:
      val normalConstraintSet = ConstraintSet()
      normalConstraintSet.clone(constraintLayoutMain)
      normalConstraintSet.constrainWidth(fullNameTextView.id, ConstraintSet.WRAP_CONTENT)
      normalConstraintSet.constrainedWidth(fullNameTextView.id, true)
      normalConstraintSet.constrainWidth(statusTextView.id, ConstraintSet.WRAP_CONTENT)
      normalConstraintSet.constrainedWidth(statusTextView.id, true)
      normalConstraintSet.constrainWidth(joinedTextView.id, ConstraintSet.WRAP_CONTENT)
      normalConstraintSet.constrainedWidth(joinedTextView.id, true)
      normalConstraintSet.constrainWidth(cityTextView.id, ConstraintSet.WRAP_CONTENT)
      normalConstraintSet.constrainedWidth(cityTextView.id, true)
      fullNameTextView.background = null
      statusTextView.background = null
      joinedTextView.background = null
      cityTextView.background = null

      normalConstraintSet.applyTo(constraintLayoutMain)

      fullNameTextView.text = listOfNotNull(
        profile.firstName,
        profile.middleName,
        profile.secondName
      )
        .joinToString(separator = " ")

      statusTextView.text = getString(
        R.string.profile_last_seen,
        profile.lastSeen?.toTimeFormattedString()
      )

      joinedTextView.setCompoundDrawablesWithIntrinsicBounds(
        R.drawable.ic_round_calendar_today_16,
        0,
        0,
        0
      )
      joinedTextView.text = getString(
        R.string.profile_joined_on,
        profile.createAt?.toShortDateFormattedString()
      )

      cityTextView.setCompoundDrawablesWithIntrinsicBounds(
        R.drawable.ic_round_location_on_24,
        0,
        0,
        0
      )
      cityTextView.text = profile.city

      overviewTextView.text = profile.overview
      overviewSubcardLayout.visibility = if (profile.overview != null)
        View.VISIBLE
      else
        View.GONE

      bornValueTextView.text = profile.birthDate?.toFullyFormattedString()
      bornSubcardLayout.visibility = if (profile.birthDate != null)
        View.VISIBLE
      else
        View.GONE

      statusValueTextView.text = profile.relationshipStatus?.toCode()?.let { getString(it) }
      statusSubcardLayout.visibility = if (profile.relationshipStatus != null)
        View.VISIBLE
      else
        View.GONE

      friendStatusImageView.visibility = if (profile.attitude == "FRIEND")
        View.VISIBLE
      else
        View.GONE

      Glide.with(this@ProfileActivity)
        .load(profile.avatarLink)
        .placeholder(GlidePlaceholder.instance)
        .error(
          ContextCompat.getDrawable(
            this@ProfileActivity,
            R.drawable.ic_round_no_accounts_24
          )
        )
        .circleCrop()
        .into(avatarImageView)
    }

    // Friends card initializing:
    val friendLayout = binding.constraintLayoutCardFriend
    val friendCountTextView = binding.friendCountTextView

    val adapter = ProfileHorizontalAdapter(this)

    vm.profileFriends.observe(this) { pagingData ->
      lifecycleScope.launch {
        adapter.submitData(pagingData)
      }
    }

    adapter.addLoadStateListener { loadState ->
      when (loadState.refresh) {
        is LoadState.Loading -> {
          friendLayout.visibility = View.GONE
        }
        is LoadState.NotLoading -> {
          friendLayout.visibility = View.VISIBLE
        }
        is LoadState.Error -> {}
      }
      //allCountTextView.text = "0 Subscribers | ${adapter.itemCount} Friends"
      friendCountTextView.text = adapter.itemCount.toString()
    }

    val friendRecyclerView = binding.friendListRecyclerView
    friendRecyclerView.layoutManager = LinearLayoutManager(
      this,
      LinearLayoutManager.HORIZONTAL,
      false
    )
    friendRecyclerView.adapter = adapter

    vm.error.observe(this) { error ->
      Log.e("ProfileActivity/op", "Error when calling method ${error.messageCode} ${error.fieldError}")
      Toast.makeText(this, "Got error = ${error.messageCode}", Toast.LENGTH_LONG).show()
    }
  }

  private fun getString(idAsString: String): String {
    return resources.getString(resources.getIdentifier(idAsString, "string", packageName))
  }

  companion object {

    const val INTENT_ARG_USERNAME = "intent_arg_username"

    fun open(
      context: Context,
      username: String? = null,
    ) {
      val intent = Intent(context, ProfileActivity::class.java).apply {
        putExtra(INTENT_ARG_USERNAME, username)
      }
      context.startActivity(intent)
    }
  }

}
