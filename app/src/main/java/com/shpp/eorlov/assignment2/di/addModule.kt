package com.shpp.eorlov.assignment2.di

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.shpp.eorlov.assignment2.utils.Constants
import com.shpp.eorlov.assignment2.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule = listOf(viewModel, repository)