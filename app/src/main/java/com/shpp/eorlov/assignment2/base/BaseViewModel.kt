package com.shpp.eorlov.assignment2.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.shpp.eorlov.assignment2.utils.PreferenceStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class BaseViewModel(application: Application) : AndroidViewModel(application), KoinComponent {
    val sharedPreferences: PreferenceStorage by inject()
    val context: Context by lazy { getApplication<Application>().applicationContext }
}