package com.shpp.eorlov.assignment2.ui.dialogfragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shpp.eorlov.assignment2.databinding.AddContactDialogBinding
import com.shpp.eorlov.assignment2.model.UserModel
import com.shpp.eorlov.assignment2.validator.Validator
import javax.inject.Inject

class ContactDialogFragmentViewModel @Inject constructor() : ViewModel() {


    //TODO replace it with sharedViewModel
    val newUser = MutableLiveData<UserModel>()


    /**
     * Adds item to dataset in the end of list
     */
    fun addItem(newUser: UserModel) {
        this.newUser.value = newUser
        this.newUser.value = this.newUser.value
    }


    /**
     * Return true if all field in add contact's dialog are valid, otherwise false
     */
    //Fixme remove UI from this class
    fun canAddContact(dialogBinding: AddContactDialogBinding, validator: Validator): Boolean {
        dialogBinding.apply {
            return (getErrorMessage(
                textInputEditTextAddress,
                textInputLayoutAddress,
                ContactDialogFragment.ValidateOperation.EMPTY,
                validator
            ) == "" &&
                    getErrorMessage(
                        textInputEditTextBirthdate,
                        textInputLayoutBirthdate,
                        ContactDialogFragment.ValidateOperation.BIRTHDAY,
                        validator
                    ) == "" &&
                    getErrorMessage(
                        textInputEditTextCareer,
                        textInputLayoutCareer,
                        ContactDialogFragment.ValidateOperation.EMPTY,
                        validator
                    ) == "" &&
                    getErrorMessage(
                        textInputEditTextEmail,
                        textInputLayoutEmail,
                        ContactDialogFragment.ValidateOperation.EMAIL,
                        validator
                    ) == "" &&
                    getErrorMessage(
                        textInputEditTextUsername,
                        textInputLayoutUsername,
                        ContactDialogFragment.ValidateOperation.EMPTY,
                        validator
                    ) == "" &&
                    getErrorMessage(
                        textInputEditTextPhone,
                        textInputLayoutPhone,
                        ContactDialogFragment.ValidateOperation.PHONE_NUMBER,
                        validator
                    ) == "")

        }
    }

    /**
     * Returns empty string if given edit text has valid input
     * otherwise returns error message
     */
    private fun getErrorMessage(
        editText: TextInputEditText,
        textInputLayout: TextInputLayout,
        validateOperation: ContactDialogFragment.ValidateOperation,
        validator: Validator,
    ): String {
        val errorMessage = when (validateOperation) {
            ContactDialogFragment.ValidateOperation.EMAIL -> validator.validateEmail(editText.text.toString())
            ContactDialogFragment.ValidateOperation.PHONE_NUMBER -> validator.validatePhoneNumber(
                editText.text.toString()
            )
            ContactDialogFragment.ValidateOperation.BIRTHDAY -> validator.validateBirthdate(editText.text.toString())
            else -> validator.checkIfFieldIsNotEmpty(editText.text.toString())
        }
        textInputLayout.error = errorMessage
        return errorMessage
    }
}