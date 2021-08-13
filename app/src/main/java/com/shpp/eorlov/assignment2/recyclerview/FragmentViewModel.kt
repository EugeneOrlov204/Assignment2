package com.shpp.eorlov.assignment2.recyclerview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shpp.eorlov.assignment2.base.BaseViewModel
import com.shpp.eorlov.assignment2.model.UserModel
import kotlin.random.Random


class FragmentViewModel : BaseViewModel() {

    val userListLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())
    val errorEvent = MutableLiveData<String>()


    /**
     * Returns value of dataset
     */
    fun getPersonData() {
        if(userListLiveData.value == null) {
            errorEvent.value = errorMessage
        }
        else if (userListLiveData.value?.isEmpty() == true) {
            userListLiveData.value = loadPersonData()
        }
    }

    /**
     * Returns item from dataset
     */
    fun getItem(position: Int): UserModel? {
        return userListLiveData.value?.get(position)
    }

    /**
     *  Removes item by clicking to button
     */
    fun removeItem(position: Int) {
        userListLiveData.value?.removeAt(position)
        userListLiveData.value = userListLiveData.value
    }

    /**
     * Adds item to dataset by given position
     */
    fun addItem(position: Int, addedItem: UserModel) {
        userListLiveData.value?.add(position, addedItem)
        userListLiveData.value = userListLiveData.value
    }

    /**
     * Adds item to dataset in the end of list
     */
    fun addItem(addedItem: UserModel) {
        userListLiveData.value?.add(addedItem)
        userListLiveData.value = userListLiveData.value
    }

    /**
     * Returns list of person's data
     * Temporary hardcoded
     */
    private fun loadPersonData(): MutableList<UserModel> {
        val listOfNames: List<String> = getNames()
        val listOfCareers: List<String> = getCareers()
        val listOfEmails: List<String> = getEmails()
        val listOfResidence: List<String> = getResidence()
        val urlOfPhoto = "https://i.pravatar.cc/"
        val result = mutableListOf<UserModel>()

        for (i in 0..9) {
            result.add(
                UserModel(
                    listOfNames[i],
                    listOfCareers[i],
                    urlOfPhoto + Random.nextInt(1000),
                    listOfResidence[i],
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
     * Temporary hardcoded
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
     * Temporary hardcoded
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
     * Temporary hardcoded
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

    /**
     * Returns list of residence
     * Temporary hardcoded
     */
    private fun getResidence(): List<String> {
        return listOf(
            "5295 Gaylord Walks Apk. 110",
            "5295 Gaylord Walks Apk. 111",
            "5295 Gaylord Walks Apk. 112",
            "5295 Gaylord Walks Apk. 113",
            "5295 Gaylord Walks Apk. 114",
            "5295 Gaylord Walks Apk. 115",
            "5295 Gaylord Walks Apk. 116",
            "5295 Gaylord Walks Apk. 117",
            "5295 Gaylord Walks Apk. 118",
            "5295 Gaylord Walks Apk. 119",
        )
    }

    companion object ErrorMessage {
        const val errorMessage = "Can't load data"
    }
}