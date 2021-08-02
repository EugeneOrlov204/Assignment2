package com.shpp.eorlov.assignment2.di

import com.shpp.eorlov.assignment2.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {

    // MyViewModel ViewModel
    viewModel { MainViewModel() }
}