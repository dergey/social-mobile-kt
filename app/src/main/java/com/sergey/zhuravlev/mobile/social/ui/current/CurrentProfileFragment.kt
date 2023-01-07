package com.sergey.zhuravlev.mobile.social.ui.current

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sergey.zhuravlev.mobile.social.R
import com.sergey.zhuravlev.mobile.social.databinding.FragmentCurrentProfileBinding
import com.sergey.zhuravlev.mobile.social.ui.common.DataFormatters.toFullyFormattedString
import com.sergey.zhuravlev.mobile.social.ui.common.DataFormatters.toShortDateFormattedString
import com.sergey.zhuravlev.mobile.social.ui.common.GlidePlaceholder
import com.sergey.zhuravlev.mobile.social.ui.common.ProfileHorizontalAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrentProfileFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val vm by viewModel<CurrentProfileViewModel>()

    val binding = FragmentCurrentProfileBinding.inflate(inflater, container, false)
    val root: View = binding.root

    // Header initializing:
    val fullNameTextView = binding.fullnameTextView
    val cityTextView = binding.cityTextView
    val joinedTextView = binding.joinedTextView
    val avatarImageView = binding.avatarImageView

    val overviewTextView = binding.overviewTextView
    val overviewSubcardLayout = binding.overviewSubcardLayout
    val bornValueTextView = binding.bornValueTextView
    val bornSubcardLayout = binding.bornSubcardLayout
    val statusValueTextView = binding.statusValueTextView
    val statusSubcardLayout = binding.statusSubcardLayout

    vm.currentProfile.observe(viewLifecycleOwner) { profile ->

      fullNameTextView.text = listOfNotNull(
        profile.firstName,
        profile.middleName,
        profile.secondName
      )
        .joinToString(separator = " ")

      joinedTextView.text = getString(
        R.string.profile_joined_on,
        profile.createAt?.toShortDateFormattedString()
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

      Glide.with(this@CurrentProfileFragment)
        .load(profile.avatarLink)
        .placeholder(GlidePlaceholder.instance)
        .error(
          ContextCompat.getDrawable(
            requireContext(),
            R.drawable.ic_round_no_accounts_24
          )
        )
        .circleCrop()
        .into(avatarImageView)
    }

    // Friends card initializing:
    val allCountTextView = binding.allCountTextView

    val friendLayout = binding.constraintLayoutCardFriend
    val findFriendButton = binding.findFriendButton
    val friendCountTextView = binding.friendCountTextView
    val friendRequestCountTextView = binding.friendRequestCountTextView
    val friendOpenLayout = binding.friendOpenLayout

    val adapter = ProfileHorizontalAdapter(requireActivity())

    vm.friends.observe(viewLifecycleOwner) { pagingData ->
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
          if (adapter.itemCount <= 0) {
            findFriendButton.visibility = View.VISIBLE
          } else {
            findFriendButton.visibility = View.INVISIBLE
          }
        }
        is LoadState.Error -> {}
      }
      allCountTextView.text = "0 Subscribers | ${adapter.itemCount} Friends"
      friendCountTextView.text = adapter.itemCount.toString()
    }
    val friendRecyclerView = binding.friendListRecyclerView
    friendRecyclerView.layoutManager = LinearLayoutManager(
      activity,
      LinearLayoutManager.HORIZONTAL,
      false
    )

    friendRecyclerView.adapter = adapter

//    vm.incomingFriendRequestCount.observe(viewLifecycleOwner) { incomingFriendRequestCount ->
//      if (incomingFriendRequestCount > 0) {
//        friendRequestCountTextView.text = incomingFriendRequestCount.toString()
//        friendRequestCountTextView.visibility = View.VISIBLE
//        friendOpenLayout.setBackgroundResource(R.drawable.background_error_layout)
//      } else {
//        friendRequestCountTextView.visibility = View.INVISIBLE
//        friendOpenLayout.background = null
//      }
//    }

    friendOpenLayout.setOnClickListener {
//      val intent = Intent(activity, FriendActivity::class.java)
//      startActivity(intent)
    }

    vm.error.observe(viewLifecycleOwner) { error ->
      Log.e("CurrentProfileFragment/op", "Error when calling method ${error.messageCode} ${error.fieldError}")
      Toast.makeText(requireContext(), "Got error = ${error.messageCode}", Toast.LENGTH_LONG).show()
    }

    return root
  }

  private fun getString(idAsString: String): String {
    val res = requireContext().resources
    return res.getString(res.getIdentifier(idAsString, "string", requireContext().packageName))
  }
}