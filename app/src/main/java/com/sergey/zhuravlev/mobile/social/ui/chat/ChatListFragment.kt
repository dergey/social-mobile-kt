package com.sergey.zhuravlev.mobile.social.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergey.zhuravlev.mobile.social.databinding.FragmentChatListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatListFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val vm by viewModel<ChatListViewModel>()

    val binding = FragmentChatListBinding.inflate(inflater, container, false)
    val root: View = binding.root

    vm.error.observe(requireActivity()) { error ->
      Toast.makeText(requireContext(), "Got error = ${error.messageCode}", Toast.LENGTH_LONG).show()
    }

    val constraintLayout = binding.constraintLayout
    val noChatLayout = binding.noChatLayout
    val statusTextView = binding.statusTextView
    val statusImageView = binding.statusImageView
    val statusActionButton = binding.statusActionButton

    // Adapter initialize:
    val adapter = ChatAdapter(requireContext())
    adapter.addLoadStateListener {
//      when (it.refresh) {
//        is LoadState.Loading ->
//      }
    }

    statusActionButton.setOnClickListener {
      adapter.retry()
    }

    val recyclerView = binding.chatPreview
    recyclerView.layoutManager = LinearLayoutManager(
      activity,
      LinearLayoutManager.VERTICAL,
      false
    )
    recyclerView.adapter = adapter

    vm.chats.observe(viewLifecycleOwner) {
        pagingData -> adapter.submitData(this.lifecycle, pagingData)
    }

    vm.error.observe(viewLifecycleOwner) { error ->
        Log.e("ChatListFragment/op", "Error when calling method ${error.messageCode} ${error.fieldError}")
        Toast.makeText(requireContext(), "Got error = ${error.messageCode}", Toast.LENGTH_LONG).show()
    }

    return root
  }

}