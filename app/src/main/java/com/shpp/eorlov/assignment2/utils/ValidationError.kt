package com.shpp.eorlov.assignment2.utils

sealed class ValidationError
data class InvalidBirthdate(val message: String) : ValidationError()
data class EmptyFieldError(val message: String) : ValidationError()
data class InvalidPhoneNumber(val message: String) : ValidationError()
data class InvalidEmail(val message: String) : ValidationError()
object NotAnError : ValidationError()

fun evaluateErrorMessage(validationError: ValidationError): String = when (validationError) {
    is InvalidBirthdate -> validationError.message
    is EmptyFieldError -> validationError.message
    is InvalidPhoneNumber -> validationError.message
    is InvalidEmail -> validationError.message
    NotAnError -> ""
}

