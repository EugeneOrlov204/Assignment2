package com.shpp.eorlov.assignment2.db

import com.shpp.eorlov.assignment2.model.UserModel

interface LocalDB {

    fun loadPersonData(): MutableList<UserModel>

    fun getCareers(): List<String>

    fun getNames(): List<String>

    fun getEmails(): List<String>

    fun getResidence(): List<String>
}