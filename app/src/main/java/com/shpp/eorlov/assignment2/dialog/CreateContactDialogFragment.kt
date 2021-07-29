package com.shpp.eorlov.assignment2.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.shpp.eorlov.assignment2.R

class CreateContactDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        return builder
            .setView(R.layout.add_contact_dialog)
            .create()
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }
}