package com.sergey.zhuravlev.mobile.social.ui.message

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DeleteMessageDialog(
  private val vm: MessageListViewModel,
  private val messageId: Long
) : DialogFragment() {

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return activity?.let {
      // Use the Builder class for convenient dialog construction
      val builder = AlertDialog.Builder(it)
      builder
        .setTitle("Delete this message?")
        .setMessage("This message will remain with the person you are talking to")
        .setPositiveButton("Delete") { dialog, id ->
          vm.deleteMessage(messageId)
        }
        .setNegativeButton("Cancel") { dialog, id ->
        }
      // Create the AlertDialog object and return it
      builder.create()
    } ?: throw IllegalStateException("Activity cannot be null")
  }
}