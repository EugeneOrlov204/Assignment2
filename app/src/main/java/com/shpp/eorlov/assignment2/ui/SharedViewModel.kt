package com.shpp.eorlov.assignment2.ui

import androidx.lifecycle.MutableLiveData
import com.shpp.eorlov.assignment2.base.BaseViewModel
import com.shpp.eorlov.assignment2.model.UserModel

class SharedViewModel : BaseViewModel() {

    val newUser = MutableLiveData<UserModel>()
}