package com.shpp.eorlov.assignment2.validator

import android.content.Context
import android.util.Patterns
import com.shpp.eorlov.assignment2.R
import com.shpp.eorlov.assignment2.utils.Constants.DATE_FORMAT
import com.shpp.eorlov.assignment2.utils.Constants.DATE_REGEX_PATTERN
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Validator(private val context: Context?) {

    private val errorMessage = ErrorMessage()


    fun validateEmail(
        _value: String
    ): String {
        return if (_value.trim { it <= ' ' }.isEmpty()) {
            errorMessage.emptyFile
        } else {
            val isValid = Patterns.EMAIL_ADDRESS.matcher(_value).matches()
            if (!isValid) {
                errorMessage.invalidEmail
            } else {
                ""
            }
        }
    }

    fun checkIfFieldIsNotEmpty(
        _value: String
    ): String {
        return if (_value.trim { it <= ' ' }.isEmpty()) {
            errorMessage.emptyFile
        } else {
            ""
        }
    }

    fun validatePhoneNumber(
        _value: String
    ): String {
        return if (_value.trim { it <= ' ' }.isEmpty()) {
            ""
        } else {
            val isValid = Patterns.PHONE.matcher(_value).matches()
            if (!isValid) {
                errorMessage.invalidPhoneNumber
            } else {
                ""
            }
        }
    }

    fun validateBirthdate(
        _value: String
    ): String {
        if (_value.trim { it <= ' ' }.isEmpty()) {
            return errorMessage.emptyFile
        } else {
            val isValid = _value.matches(
                Regex(DATE_REGEX_PATTERN)
            )
            return if (!isValid) {
                errorMessage.invalidBirthdate
            } else {
                val format = SimpleDateFormat(DATE_FORMAT, Locale.CANADA_FRENCH)
                try {
                    format.parse(_value)
                    ""
                } catch (e: ParseException) {
                    errorMessage.invalidBirthdate
                }
            }
        }
    }

    inner class ErrorMessage {
        val invalidBirthdate = context?.getString(R.string.invalid_birthdate) ?: ""
        val emptyFile = context?.getString(R.string.empty_field) ?: ""
        val invalidPhoneNumber = context?.getString(R.string.invalid_phone_number) ?: ""
        val invalidEmail = context?.getString(R.string.invalid_email) ?: ""
    }
}