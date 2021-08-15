package com.shpp.eorlov.assignment2.validator

import android.util.Patterns
import com.shpp.eorlov.assignment2.R
import com.shpp.eorlov.assignment2.utils.Constants.DATE_FORMAT
import com.shpp.eorlov.assignment2.utils.Constants.DATE_REGEX_PATTERN
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Validator {

    fun validateEmail(
        _value: String
    ): String {
        return if (_value.trim { it <= ' ' }.isEmpty()) {
            ErrorMessage.EMPTY_FILE
        }  else {
            val isValid = Patterns.EMAIL_ADDRESS.matcher(_value).matches()
            if (!isValid) {
                ErrorMessage.INVALID_EMAIL
            } else {
                ""
            }
        }
    }

    fun checkIfFieldIsNotEmpty(
        _value: String
    ): String {
        return if (_value.trim { it <= ' ' }.isEmpty()) {
            ErrorMessage.EMPTY_FILE
        } else {
            ""
        }
    }

    fun validatePhoneNumber(
        _value: String
    ): String {
        return if (_value.trim { it <= ' ' }.isEmpty()) {
            ""
        }
        else {
            val isValid = Patterns.PHONE.matcher(_value).matches()
            if (!isValid) {
                ErrorMessage.INVALID_PHONE_NUMBER
            } else {
                ""
            }
        }
    }

    fun validateBirthdate(
        _value: String
    ): String {
        if (_value.trim { it <= ' ' }.isEmpty()) {
            return ErrorMessage.EMPTY_FILE
        } else {
            val isValid = _value.matches(
                Regex(DATE_REGEX_PATTERN)
            )
            return if (!isValid) {
                ErrorMessage.INVALID_DATE
            } else {
                val format = SimpleDateFormat(DATE_FORMAT, Locale.CANADA_FRENCH)
                try {
                    format.parse(_value)
                    ""
                } catch (e: ParseException) {
                    ErrorMessage.INVALID_DATE
                }
            }
        }
    }

    object ErrorMessage {
        const val INVALID_DATE = "Invalid birthdate, ex: 01/01/2021"
        const val EMPTY_FILE = "This field should not be empty"
        const val INVALID_PHONE_NUMBER = "Invalid phone number, ex: (264)-654-3762"
        const val INVALID_EMAIL = "Invalid Email address, ex: abc@example.com"
    }

//    object ErrorMessage {
//        const val INVALID_DATE = context.getString(R.string.invalidate_birthdate)
//        const val EMPTY_FILE = context.getString(R.string.empty_field)
//        const val INVALID_PHONE_NUMBER = context.getString(R.string.invalid_phone_number)
//        const val INVALID_EMAIL = context.getString(R.string.invalid_email)
//    }
}