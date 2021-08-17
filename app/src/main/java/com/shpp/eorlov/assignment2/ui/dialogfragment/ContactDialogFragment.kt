package com.shpp.eorlov.assignment2.ui.dialogfragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
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
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.eorlov.assignment2.R
import com.shpp.eorlov.assignment2.databinding.AddContactDialogBinding
import com.shpp.eorlov.assignment2.model.UserModel
import com.shpp.eorlov.assignment2.ui.SharedViewModel
import com.shpp.eorlov.assignment2.utils.Constants
import com.shpp.eorlov.assignment2.utils.Constants.DIALOG_FRAGMENT_REQUEST_KEY
import com.shpp.eorlov.assignment2.utils.Constants.NEW_CONTACT_KEY
import com.shpp.eorlov.assignment2.utils.ext.clicks
import com.shpp.eorlov.assignment2.utils.ext.loadImage
import com.shpp.eorlov.assignment2.validator.Validator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import kotlin.math.abs


class ContactDialogFragment : DialogFragment() {
    private lateinit var dialogBinding: AddContactDialogBinding
    private var pathToLoadedImageFromGallery: String = ""
    private val viewModel: ContactDialogFragmentViewModel by inject()
    private val sharedViewModel: SharedViewModel by inject()

    private var imageLoaderLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val imageView: AppCompatImageView = dialogBinding.imageViewPersonPhoto
            if (result.resultCode == RESULT_OK && result.data?.data != null) {
                val imageData = result.data?.data ?: return@registerForActivityResult
                pathToLoadedImageFromGallery = imageData.toString()
                imageView.loadImage(imageData)
            }
        }

    enum class ValidateOperation {
        BIRTHDAY, EMAIL, PHONE_NUMBER, EMPTY
    }

    @ExperimentalCoroutinesApi
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
        setObserver()
    }

    override fun onStart() {
        super.onStart()

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

        val imageData = pathToLoadedImageFromGallery

        with(dialogBinding) {

            val newContact =
                UserModel(
                    textInputEditTextUsername.text.toString(),
                    textInputEditTextCareer.text.toString(),
                    imageData,
                    textInputEditTextAddress.text.toString(),
                    textInputEditTextBirthdate.text.toString(),
                    textInputEditTextPhone.text.toString(),
                    textInputEditTextEmail.text.toString()
                )

            viewModel.addItem(newContact)

            val bundle = Bundle()
            bundle.putParcelable(NEW_CONTACT_KEY, newContact)
            setFragmentResult(DIALOG_FRAGMENT_REQUEST_KEY, bundle)
        }
        pathToLoadedImageFromGallery = ""
        dismiss()
    }

    private fun setObserver() {
        viewModel.newUser.observe(viewLifecycleOwner) { user ->
            user?.let { sharedViewModel.newUser.value = user }
        }
    }

    /**
     * Initialize date and set listeners to EditTexts
     */
    private fun initializeData() {
        dialogBinding.imageViewPersonPhoto.loadImage(R.mipmap.ic_launcher)
    }

    private var previousClickTimestamp = SystemClock.uptimeMillis()

    @ExperimentalCoroutinesApi
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


            imageViewImageLoader.clicks()
                .onEach {
                    if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                        loadImageFromGallery()
                        previousClickTimestamp = SystemClock.uptimeMillis()
                    }
                }
                .launchIn(lifecycleScope)


            imageButtonContactDialogCloseButton.clicks()
                .onEach {
                    if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                        dismiss()
                        previousClickTimestamp = SystemClock.uptimeMillis()
                    }
                }
                .launchIn(lifecycleScope)


            buttonSave.clicks()
                .onEach {
                    if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                        addContact()
                        previousClickTimestamp = SystemClock.uptimeMillis()
                    }
                }
                .launchIn(lifecycleScope)

        }
    }

    private fun loadImageFromGallery() {
        val gallery = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )
        imageLoaderLauncher.launch(gallery)
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
