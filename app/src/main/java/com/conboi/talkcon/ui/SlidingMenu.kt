package com.conboi.talkcon.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.conboi.talkcon.R

class SlidingMenu : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext(), R.style.MyCustomTheme).setTitle("dawdawd").create()

    companion object {
        const val TAG = "slidingMenu"
    }
}