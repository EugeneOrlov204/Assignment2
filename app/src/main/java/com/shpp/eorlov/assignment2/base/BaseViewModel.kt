package com.shpp.eorlov.assignment2.base

import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment2.utils.PreferenceStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class BaseViewModel : ViewModel(), KoinComponent {
    val sharedPreferences: PreferenceStorage by inject()
}