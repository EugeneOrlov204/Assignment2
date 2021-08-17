package com.shpp.eorlov.assignment2.di

import com.shpp.eorlov.assignment2.utils.PreferenceStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

val repository : Module = module {

    single {
        PreferenceStorage(androidApplication())
    }
}
