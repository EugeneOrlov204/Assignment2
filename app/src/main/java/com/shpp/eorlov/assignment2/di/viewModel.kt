package com.shpp.eorlov.assignment2.di

import com.shpp.eorlov.assignment2.recyclerview.RecyclerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModel: Module = module {
    viewModel { RecyclerViewModel() }
}