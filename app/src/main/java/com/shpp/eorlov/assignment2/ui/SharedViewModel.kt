package com.shpp.eorlov.assignment2.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment2.model.UserModel
import javax.inject.Inject

class SharedViewModel @Inject constructor() : ViewModel() {
    val newUser = MutableLiveData<UserModel>(null)
}