package com.shpp.eorlov.assignment2.validator

import android.util.Patterns
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
            "This field should not be empty"
        }  else {
            val isValid = Patterns.EMAIL_ADDRESS.matcher(_value).matches()
            if (!isValid) {
                "Invalid Email address, ex: abc@example.com"
            } else {
                ""
            }
        }
    }

    fun checkIfFieldIsNotEmpty(
        _value: String
    ): String {
        return if (_value.trim { it <= ' ' }.isEmpty()) {
            "This field should not be empty"
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
                "Invalid phone number, ex: (264)-654-3762"
            } else {
                ""
            }
        }
    }

    fun validateBirthdate(
        _value: String
    ): String {
        if (_value.trim { it <= ' ' }.isEmpty()) {
            return "This field should not be empty"
        } else {
            val isValid = _value.matches(
                Regex(DATE_REGEX_PATTERN)
            )
            return if (!isValid) {
                "Invalid birthdate, ex: 01/01/2021"
            } else {
                val format = SimpleDateFormat(DATE_FORMAT, Locale.CANADA_FRENCH)
                try {
                    format.parse(_value)
                    ""
                } catch (e: ParseException) {
                    "Invalid birthdate, ex: 01/01/2021"
                }
            }
        }
    }
}