package com.shpp.eorlov.assignment2.validator

import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.material.textfield.TextInputLayout as TextInputLayout1

class Validator {
    fun validateEmail(
        editText: TextInputEditText,
        errorMessageLayout: TextInputLayout1
    ): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.isEmpty()) {
            errorMessageLayout.error = ""
        } else {
            val emailId = editText.text.toString()
            val isValid = Patterns.EMAIL_ADDRESS.matcher(emailId).matches()
            if (!isValid) {
                errorMessageLayout.error = "Invalid Email address, ex: abc@example.com"
                return false
            } else {
                errorMessageLayout.error = ""
            }
        }
        return true
    }

    fun validateEmptyField(
        editText: TextInputEditText,
        errorMessageLayout: TextInputLayout1
    ): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.isEmpty()) {
            errorMessageLayout.error = "This field should not be empty"
            return false
        }

        errorMessageLayout.error = ""
        return true
    }

    fun validatePhoneNumber(
        editText: TextInputEditText,
        errorMessageLayout: TextInputLayout1
    ): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.isEmpty()) {
            errorMessageLayout.error = ""
        } else {
            val phoneNumber = editText.text.toString()
            val isValid = Patterns.PHONE.matcher(phoneNumber).matches()
            if (!isValid) {
                errorMessageLayout.error = "Invalid phone number, ex: (264)-654-3762"
                return false
            } else {
                errorMessageLayout.error = ""
            }
        }
        return true
    }

    fun validateBirthdate(
        editText: TextInputEditText,
        errorMessageLayout: TextInputLayout1
    ): Boolean {

        if (editText.text.toString().trim { it <= ' ' }.isEmpty()) {
            errorMessageLayout.error = "This field should not be empty"
            return false
        } else {
            val date = editText.text.toString()
            val isValid = date.matches(
                Regex("^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$")
            )
            return if (!isValid) {
                errorMessageLayout.error = "Invalid birthdate, ex: 01/01/2021"
                false
            } else {
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.CANADA_FRENCH)
                try {
                    format.parse(date)
                    errorMessageLayout.error = ""
                    true
                } catch (e: ParseException) {
                    errorMessageLayout.error = "Invalid birthdate, ex: 01/01/2021"
                    false
                }
            }
        }

    }
}