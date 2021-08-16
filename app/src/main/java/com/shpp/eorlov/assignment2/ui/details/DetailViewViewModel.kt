package com.shpp.eorlov.assignment2.ui.details

import androidx.lifecycle.MutableLiveData
import com.shpp.eorlov.assignment2.base.BaseViewModel
import com.shpp.eorlov.assignment2.model.UserModel

class DetailViewViewModel : BaseViewModel() {

    val userListLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())
    val errorEvent = MutableLiveData<String>()
}