package com.shpp.eorlov.assignment2.ui.mainfragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment2.model.UserModel
import javax.inject.Inject


class MainFragmentViewModel @Inject constructor() : ViewModel() {

    val userListLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())
    val errorEvent = MutableLiveData<String>()
    var isInitialized = false



    /**
     * Returns value of dataset
     */
    fun getPersonData() {
        if (userListLiveData.value == null) {
            errorEvent.value = errorEvent.value
        } else if (!isInitialized) {
            userListLiveData.value = userListLiveData.value
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