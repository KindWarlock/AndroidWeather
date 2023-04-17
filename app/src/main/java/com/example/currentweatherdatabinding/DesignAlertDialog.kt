package com.example.currentweatherdatabinding

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DesignAlertDialog: DialogFragment() {
    companion object {
        const val SHORT_VIEW = 0
        const val DETAILED_VIEW = 1
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val items = arrayOf(getString(R.string.viewShort), getString(R.string.viewDetailed))
        val isDetailed: Boolean = arguments?.getBoolean("isDetailed") ?: false
        return activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.viewTitle))
                .setSingleChoiceItems(items,
                    if (!isDetailed) SHORT_VIEW else DETAILED_VIEW,
                    activity as DialogInterface.OnClickListener
                )
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}