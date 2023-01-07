package com.sergey.zhuravlev.mobile.social.ui.message

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.sergey.zhuravlev.mobile.social.R
import com.sergey.zhuravlev.mobile.social.data.api.SocialApi
import com.sergey.zhuravlev.mobile.social.databinding.ActivityMessageListBinding
import com.sergey.zhuravlev.mobile.social.domain.model.ChatModel
import com.sergey.zhuravlev.mobile.social.domain.model.ProfileModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MessageListActivity : AppCompatActivity() {

  private val vm: MessageListViewModel by viewModel()

  @RequiresApi(Build.VERSION_CODES.Q)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    vm.parseIntent(intent)

    val binding = ActivityMessageListBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // ActionBar initializing:
    val inflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val actionBarCustomView: View = inflater.inflate(R.layout.actionbar_activity_message, null)

    val actionBar: ActionBar = supportActionBar ?: throw IllegalArgumentException("ActionBar not set")
    val params = ActionBar.LayoutParams(
      ActionBar.LayoutParams.MATCH_PARENT,
      ActionBar.LayoutParams.MATCH_PARENT,
      Gravity.START
    )
    actionBar.setCustomView(actionBarCustomView, params)
    actionBar.setDisplayShowCustomEnabled(true)

    val actionBarAvatarImageView =
      actionBarCustomView.findViewById<ImageView>(R.id.avatar_image_view)
    val actionBarFullnameTextView =
      actionBarCustomView.findViewById<TextView>(R.id.fullname_text_view)
    val actionBarStatusTextView = actionBarCustomView.findViewById<TextView>(R.id.status_text_view)

    val profile: ProfileModel = vm.chat.targetProfile

    actionBarFullnameTextView.text = listOfNotNull(
      profile.firstName,
      profile.middleName,
      profile.secondName
    )
      .joinToString(separator = " ")

    actionBarStatusTextView.text = getString(R.string.profile_last_seen, profile.lastSeen)

    val glideUrl = GlideUrl(
      profile.avatarLink,
      LazyHeaders.Builder()
        .addHeader("Authorization", "Bearer " + SocialApi.getBearerToken())
        .build()
    )
    Glide.with(this)
      .load(glideUrl)
      .circleCrop()
      .into(actionBarAvatarImageView)

    // Messages initializing:
    val adapter = MessageAdapter(this)

    val recyclerView = binding.messageRecyclerView
    val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
    recyclerView.layoutManager = linearLayoutManager
    recyclerView.adapter = adapter

    vm.messages.observe(this) { pagingData ->
      adapter.submitData(lifecycle, pagingData)
    }

    // Text initializing:

    val editText = binding.messageEditText

    val sendMessageButton = binding.sendMessageButton
    sendMessageButton.setOnClickListener {
      vm.createTextMessage(editText.text.toString())
      editText.text.clear()
    }

    // Image initializing:

    val addMessageButton = binding.addMessageButton
    val onMenuItemClickListener =
      PopupMenu.OnMenuItemClickListener { item ->
        when (item.itemId) {
          R.id.image_attachment_menu_item -> {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
              Intent.createChooser(intent, "Select Picture"),
              IMAGE_CHOOSE_REQUEST
            )
            true
          }
          R.id.sticker_attachment_menu_item -> true
          else -> false
        }
      }
    addMessageButton.setOnClickListener { v ->
      val popup = PopupMenu(this@MessageListActivity, v)
      popup.setForceShowIcon(true)
      popup.setOnMenuItemClickListener(onMenuItemClickListener)
      val inflater = popup.menuInflater
      inflater.inflate(R.menu.message_attachment_menu, popup.menu)
      popup.show()
    }


    // Read all message (mb move to vm logic?):

    vm.updateReadStatus()
    vm.subscribeMessages()
  }

  @Deprecated("Deprecated in Java")
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == RESULT_OK) {
      when (requestCode) {
        IMAGE_CHOOSE_REQUEST -> {
          val selectedImageUri = data?.data
          if (selectedImageUri != null) {
            vm.createImageMessage(selectedImageUri)
          }
        }
      }
    }
  }

  companion object {

    const val INTENT_ARG_CHAT = "intent_arg_chat_id"
    const val IMAGE_CHOOSE_REQUEST = 101

    fun open(
      context: Context,
      chat: ChatModel? = null,
    ) {
      val intent = Intent(context, MessageListActivity::class.java).apply {
        putExtra(INTENT_ARG_CHAT, chat)
      }
      context.startActivity(intent)
    }
  }
}
