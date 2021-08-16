package com.shpp.eorlov.assignment2.ui.dialogfragment

import androidx.lifecycle.MutableLiveData
import com.shpp.eorlov.assignment2.base.BaseViewModel
import com.shpp.eorlov.assignment2.model.UserModel

class ContactDialogFragmentViewModel : BaseViewModel() {

    val newUser = MutableLiveData<UserModel>()

    /**
     * Adds item to dataset in the end of list
     */
    fun addItem(newUser: UserModel) {
        this.newUser.value = newUser
        this.newUser.value = this.newUser.value
    }
}