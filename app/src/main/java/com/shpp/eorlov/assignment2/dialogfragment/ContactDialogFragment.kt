package com.shpp.eorlov.assignment2.dialogfragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.eorlov.assignment2.PreferenceStorage
import com.shpp.eorlov.assignment2.ui.MainActivity
import com.shpp.eorlov.assignment2.R
import com.shpp.eorlov.assignment2.adapter.ContactsRecyclerAdapter
import com.shpp.eorlov.assignment2.model.UserModel
import com.shpp.eorlov.assignment2.databinding.AddContactDialogBinding
import com.shpp.eorlov.assignment2.utils.Constants
import com.shpp.eorlov.assignment2.utils.ext.loadImageUsingGlide
import com.shpp.eorlov.assignment2.validator.Validator
import com.shpp.eorlov.assignment2.viewmodel.MainViewModel


class ContactDialogFragment : DialogFragment() {
    private lateinit var dialogBinding: AddContactDialogBinding
    private lateinit var loadedImageFromGallery: PreferenceStorage
    private lateinit var sharedViewModel: MainViewModel
    private lateinit var sharedContactsAdapter: ContactsRecyclerAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AddContactDialogBinding.inflate(LayoutInflater.from(context))

        initializeData()
        setListeners()

        return AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    private fun addContact() {
        if (!canAddContact()) {
            Log.d("Create contact", "Can't create new contact")
            return
        }

        val imageData = loadedImageFromGallery.getString(Constants.PREF_NAME)

        with(dialogBinding) {
            sharedViewModel.addItem(
                UserModel(
                    textInputEditTextUsername.text.toString(),
                    textInputEditTextCareer.text.toString(),
                    imageData ?: "https://i.pravatar.cc/",
                    textInputEditTextAddress.text.toString(),
                    textInputEditTextBirthdate.text.toString(),
                    textInputEditTextPhone.text.toString(),
                    textInputEditTextEmail.text.toString()
                )
            )
        }
        loadedImageFromGallery.clearPrefs()
        dismiss()
    }

    /**
     * Initialize date and set listeners to EditTexts
     */
    private fun initializeData() {
        dialogBinding.imageViewPersonPhoto.loadImageUsingGlide(R.mipmap.ic_launcher)
        sharedViewModel = (activity as MainActivity).viewModel
        loadedImageFromGallery = sharedViewModel.sharedPreferences
        sharedContactsAdapter = (activity as MainActivity).contactsRecyclerAdapter
    }

    enum class ValidateOperation {
        BIRTHDAY, EMAIL, PHONE_NUMBER, EMPTY
    }

    private fun setListeners() {
        with(dialogBinding) {
            addListenerToEditText(
                textInputEditTextAddress,
                textInputLayoutAddress,
                ValidateOperation.EMPTY
            )
            addListenerToEditText(
                textInputEditTextBirthdate,
                textInputLayoutBirthdate,
                ValidateOperation.BIRTHDAY
            )
            addListenerToEditText(
                textInputEditTextCareer,
                textInputLayoutCareer,
                ValidateOperation.EMPTY
            )
            addListenerToEditText(
                textInputEditTextEmail,
                textInputLayoutEmail,
                ValidateOperation.EMAIL
            )
            addListenerToEditText(
                textInputEditTextUsername,
                textInputLayoutUsername,
                ValidateOperation.EMPTY
            )
            addListenerToEditText(
                textInputEditTextPhone,
                textInputLayoutPhone,
                ValidateOperation.PHONE_NUMBER
            )

            imageViewImageLoader.setOnClickListener {
                loadImageFromGallery()
            }

            imageButtonContactDialogCloseButton.setOnClickListener {
                loadedImageFromGallery.clearPrefs()
                dismiss()
            }

            buttonSave.setOnClickListener {
                addContact()
            }
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val imageView: AppCompatImageView = dialogBinding.imageViewPersonPhoto

            if (result.resultCode == RESULT_OK) {
                if (result.data?.data != null) {
                    val imageData = result.data?.data ?: return@registerForActivityResult
                    loadedImageFromGallery.save(Constants.PREF_NAME, imageData.toString())
                    imageView.loadImageUsingGlide(imageData)
                }
            }
        }

    private fun loadImageFromGallery() {
        val gallery = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )
        resultLauncher.launch(gallery)
    }

    /**
     * Set listener to given EditText
     */
    private fun addListenerToEditText(
        editText: TextInputEditText,
        addressTextInput: TextInputLayout,
        validateOperation: ValidateOperation
    ) {

        editText.addTextChangedListener {
            addressTextInput.error =
                when (validateOperation) {
                    ValidateOperation.EMAIL -> Validator.validateEmail(editText.text.toString())
                    ValidateOperation.PHONE_NUMBER -> Validator.validatePhoneNumber(editText.text.toString())
                    ValidateOperation.BIRTHDAY -> Validator.validateBirthdate(editText.text.toString())
                    ValidateOperation.EMPTY -> Validator.checkIfFieldIsNotEmpty(editText.text.toString())
                }
        }
    }

    /**
     * Returns empty string if given edit text has valid input
     * otherwise returns error message
     */
    private fun getErrorMessage(
        editText: TextInputEditText,
        textInputLayout: TextInputLayout,
        validateOperation: ValidateOperation,
    ): String {
        val errorMessage = when (validateOperation) {
            ValidateOperation.EMAIL -> Validator.validateEmail(editText.text.toString())
            ValidateOperation.PHONE_NUMBER -> Validator.validatePhoneNumber(editText.text.toString())
            ValidateOperation.BIRTHDAY -> Validator.validateBirthdate(editText.text.toString())
            else -> Validator.checkIfFieldIsNotEmpty(editText.text.toString())
        }
        textInputLayout.error = errorMessage
        return errorMessage
    }

    /**
     * Return true if all field in add contact's dialog are valid, otherwise false
     */
    private fun canAddContact(): Boolean {
        with(dialogBinding) {
            return (getErrorMessage(
                textInputEditTextAddress,
                textInputLayoutAddress,
                ValidateOperation.EMPTY
            ) == "" &&
                    getErrorMessage(
                        textInputEditTextBirthdate,
                        textInputLayoutBirthdate,
                        ValidateOperation.BIRTHDAY
                    ) == "" &&
                    getErrorMessage(
                        textInputEditTextCareer,
                        textInputLayoutCareer,
                        ValidateOperation.EMPTY
                    ) == "" &&
                    getErrorMessage(
                        textInputEditTextEmail,
                        textInputLayoutEmail,
                        ValidateOperation.EMAIL
                    ) == "" &&
                    getErrorMessage(
                        textInputEditTextUsername,
                        textInputLayoutUsername,
                        ValidateOperation.EMPTY
                    ) == "" &&
                    getErrorMessage(
                        textInputEditTextPhone,
                        textInputLayoutPhone,
                        ValidateOperation.PHONE_NUMBER
                    ) == "")

        }
    }
}
