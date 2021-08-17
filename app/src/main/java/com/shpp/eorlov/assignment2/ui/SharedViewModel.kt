package com.shpp.eorlov.assignment2.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.shpp.eorlov.assignment2.base.BaseViewModel
import com.shpp.eorlov.assignment2.model.UserModel

class SharedViewModel(application: Application) : BaseViewModel(application) {

    val newUser = MutableLiveData<UserModel>()
}