package com.shpp.eorlov.assignment2.ui.dialogfragment

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.shpp.eorlov.assignment2.base.BaseViewModel
import com.shpp.eorlov.assignment2.model.UserModel

class ContactDialogFragmentViewModel(application: Application) : BaseViewModel(application) {

    val newUser = MutableLiveData<UserModel>()

    /**
     * Adds item to dataset in the end of list
     */
    fun addItem(newUser: UserModel) {
        checksModelId()
        this.newUser.value = newUser
        this.newUser.value = this.newUser.value
    }

    private fun checksModelId() {

    }

    class ErrorMessage {

    }
}