package com.shpp.eorlov.assignment2.ui.mainfragment

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.shpp.eorlov.assignment2.R
import com.shpp.eorlov.assignment2.base.BaseViewModel
import com.shpp.eorlov.assignment2.db.ContactsDatabase
import com.shpp.eorlov.assignment2.model.UserModel


class MainFragmentViewModel(application: Application) : BaseViewModel(application) {

    val userListLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())
    val errorEvent = MutableLiveData<String>()

    private val contactsDatabase: ContactsDatabase = ContactsDatabase(context)


    /**
     * Returns value of dataset
     */
    fun getPersonData() {
        if (userListLiveData.value == null) {
            errorEvent.value = context.getString(R.string.loading_data_error_message)
        } else if (userListLiveData.value?.isEmpty() == true) {
            userListLiveData.value = contactsDatabase.listOfContacts
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
}