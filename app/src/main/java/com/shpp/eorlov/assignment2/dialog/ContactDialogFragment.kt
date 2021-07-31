package com.shpp.eorlov.assignment2.dialog

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.eorlov.assignment2.MainActivity
import com.shpp.eorlov.assignment2.R
import com.shpp.eorlov.assignment2.data.PersonData
import com.shpp.eorlov.assignment2.databinding.AddContactDialogBinding
import com.shpp.eorlov.assignment2.utils.Constants
import com.shpp.eorlov.assignment2.utils.ext.loadImageUsingGlide
import com.shpp.eorlov.assignment2.validator.Validator


class ContactDialogFragment : DialogFragment() {
    private lateinit var dialogBinding: AddContactDialogBinding
    private lateinit var settings: SharedPreferences

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AddContactDialogBinding.inflate(LayoutInflater.from(context))
        val loadImageButton = dialogBinding.imageViewImageLoader

        loadImageButton.setOnClickListener {
            val gallery = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            startActivityForResult(gallery, Constants.PICK_IMAGE)
//            val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//                val imageView: AppCompatImageView = dialogBinding.personPhotoImageView
//
//                if (resultCode == RESULT_OK && requestCode == Constants.PICK_IMAGE) {
//                    if (data?.data != null) {
//                        val imageData = data.data ?: return
//                        val prefEditor = settings.edit();
//                        prefEditor.putString(Constants.PREF_NAME, imageData.toString());
//                        prefEditor.apply();
//                        imageView.loadImageUsingGlide(imageData)
//                    }
//                }
//            }
            //FIXME registerForActivityResult
        }

        initializeDate()
        settings = this.requireActivity().getSharedPreferences(Constants.PREFS_FILE, MODE_PRIVATE)
        return AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
    }

    override fun onResume() {
        super.onResume()

        //Set size of DialogFragment to all size of parent
        if (dialog != null && dialog?.window != null) {
            val params: ViewGroup.LayoutParams =
                dialog?.window?.attributes ?: return
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.window?.attributes = params as WindowManager.LayoutParams
        }
    }

    /**
     * Add new contact to RecyclerView if all field are valid
     */
    fun addContact(): PersonData? {
        if (!canAddContact()) {
            return null
        }

        val newContact: PersonData
        with(dialogBinding) {
            newContact = PersonData(
                textInputEditTextUsername.text.toString(),
                textInputEditTextCareer.text.toString(),
                "",
                textInputEditTextAddress.text.toString(),
                textInputEditTextBirthdate.text.toString(),
                textInputEditTextPhone.text.toString(),
                textInputEditTextEmail.text.toString()
            )
        }

        dismiss()
        return newContact
    }


    /**
     * I've taken code from here: https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageView: AppCompatImageView = dialogBinding.imageViewPersonPhoto

        if (resultCode == RESULT_OK && requestCode == Constants.PICK_IMAGE) {
            if (data?.data != null) {
                val imageData = data.data ?: return
                val prefEditor = settings.edit()
                prefEditor.putString(Constants.PREF_NAME, imageData.toString())
                prefEditor.apply()
                imageView.loadImageUsingGlide(imageData)
            }
        }
    }

    /**
     * Initialize date and set listeners to EditTexts
     */
    private fun initializeDate() {
        with(dialogBinding) {
            imageViewPersonPhoto.loadImageUsingGlide(R.mipmap.ic_launcher)
            addListenerToEditText(textInputEditTextAddress, textInputLayoutAddress, "")
            addListenerToEditText(textInputEditTextBirthdate, textInputLayoutBirthdate, "birthdate")
            addListenerToEditText(textInputEditTextCareer, textInputLayoutCareer, "")
            addListenerToEditText(textInputEditTextEmail, textInputLayoutEmail, "email")
            addListenerToEditText(textInputEditTextUsername, textInputLayoutUsername, "")
            addListenerToEditText(textInputEditTextPhone, textInputLayoutPhone, "phoneNumber")
        }
    }

    /**
     * Set listener to given EditText
     */
    private fun addListenerToEditText(
        editText: TextInputEditText,
        addressTextInput: TextInputLayout,
        validateOperation: String
    ) {
        val validator = Validator()

        editText.addTextChangedListener {
            when (validateOperation) {
                "email" -> validator.validateEmail(editText, addressTextInput)
                "phoneNumber" -> validator.validatePhoneNumber(editText, addressTextInput)
                "birthdate" -> validator.validateBirthdate(editText, addressTextInput)
                else -> validator.validateEmptyField(editText, addressTextInput)
            }
        }
    }

    /**
     * Checks if given edit text has valid input
     */
    private fun isValidInput(
        editText: TextInputEditText,
        addressTextInput: TextInputLayout,
        validateOperation: String
    ): Boolean {
        val validator = Validator()
        return when (validateOperation) {
            "email" -> validator.validateEmail(editText, addressTextInput)
            "phoneNumber" -> validator.validatePhoneNumber(editText, addressTextInput)
            "birthdate" -> validator.validateBirthdate(editText, addressTextInput)
            else -> validator.validateEmptyField(editText, addressTextInput)
        }
    }

    /**
     * Return true if all field in add contact's dialog are valid, otherwise false
     */
    private fun canAddContact(): Boolean {
        with(dialogBinding) {
            return isValidInput(textInputEditTextAddress, textInputLayoutAddress, "") &&
                    isValidInput(textInputEditTextBirthdate, textInputLayoutBirthdate, "birthdate") &&
                    isValidInput(textInputEditTextCareer, textInputLayoutCareer, "") &&
                    isValidInput(textInputEditTextEmail, textInputLayoutEmail, "email") &&
                    isValidInput(textInputEditTextUsername, textInputLayoutUsername, "") &&
                    isValidInput(textInputEditTextPhone, textInputLayoutPhone, "phoneNumber")
        }
    }

}
