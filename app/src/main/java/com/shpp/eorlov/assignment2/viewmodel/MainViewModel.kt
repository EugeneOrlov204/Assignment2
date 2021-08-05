package com.shpp.eorlov.assignment2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment2.data.PersonData
import kotlin.random.Random

class MainViewModel : ViewModel() {

    val userListLiveData = MutableLiveData<MutableList<PersonData>>(mutableListOf())
    val errorEvent = MutableLiveData<String>()

    /**
     * Returns value of dataset
     */
    fun getPersonData() {
        if (userListLiveData.value == null) {
            userListLiveData.value = loadPersonData()
        } else {
            errorEvent.value = errorMessage
        }
    }

    /**
     * Returns item from dataset
     */
    fun getItem(position: Int): PersonData? {
        return userListLiveData.value?.get(position)
    }

    /**
     *  Removes item by clicking to button
     */
    fun removeItem(position: Int) {
        userListLiveData.value?.removeAt(position)
    }

    /**
     * Adds item to dataset by given position
     */
    fun addItem(position: Int, addedItem: PersonData) {
        userListLiveData.value?.add(position, addedItem)
    }

    /**
     * Adds item to dataset in the end of list
     */
    fun addItem(
        username: String,
        career: String,
        photo: String,
        residenceAddress: String,
        birthDate: String,
        phoneNumber: String,
        email: String
    ) {
        userListLiveData.value?.add(
            PersonData(
                username,
                career,
                photo,
                residenceAddress,
                birthDate,
                phoneNumber,
                email
            )
        )
    }

    /**
     * Returns list of person's data
     * Temporary hardcoded
     */
    private fun loadPersonData(): MutableList<PersonData> {
        val listOfNames: List<String> = getNames()
        val listOfCareers: List<String> = getCareers()
        val listOfEmails: List<String> = getEmails()
        val urlOfPhoto = "https://i.pravatar.cc/"
        val result = mutableListOf<PersonData>()

        for (i in 0..9) {
            result.add(
                PersonData(
                    listOfNames[i],
                    listOfCareers[i],
                    urlOfPhoto + Random.nextInt(1500),
                    "",
                    "",
                    "",
                    listOfEmails[i]
                )
            )
        }

        return result

    }


    /**
     * Returns list of careers
     */
    private fun getCareers(): List<String> {
        return listOf(
            "Community worker",
            "Estate agent",
            "Pilot",
            "Dentist",
            "Clockmaker",
            "Barrister",
            "Auctioneer",
            "Printer",
            "Comedian",
            "Car dealer"
        )
    }

    /**
     * Returns list of names
     */
    private fun getNames(): List<String> {
        return listOf(
            "Darcy Benn",
            "Tatiana Matthewson",
            "Zandra Bailey",
            "Eliot Stevenson",
            "Mina Derrickson",
            "Gyles Breckinridge",
            "Sharlene Horsfall",
            "Milton Bryson",
            "Allissa Tindall",
            "Frannie Morriss"
        )
    }

    /**
     * Returns list of names
     */
    private fun getEmails(): List<String> {
        return listOf(
            "@name1.surname@gmail.com",
            "@name2.surname@gmail.com",
            "@name3.surname@gmail.com",
            "@name4.surname@gmail.com",
            "@name5.surname@gmail.com",
            "@name6.surname@gmail.com",
            "@name7.surname@gmail.com",
            "@name8.surname@gmail.com",
            "@name9.surname@gmail.com",
            "@name10.surname@gmail.com",
        )
    }

    companion object ErrorMessage {
        const val errorMessage = "Can't load data"
    }
}