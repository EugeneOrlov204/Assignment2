package com.shpp.eorlov.assignment2.di

import com.shpp.eorlov.assignment2.ui.SharedViewModel
import com.shpp.eorlov.assignment2.ui.dialogfragment.ContactDialogFragmentViewModel
import com.shpp.eorlov.assignment2.ui.details.DetailViewViewModel
import com.shpp.eorlov.assignment2.ui.mainfragment.MainFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModel: Module = module {
    viewModel { MainFragmentViewModel() }
    viewModel { ContactDialogFragmentViewModel() }
    viewModel { SharedViewModel() }
    viewModel { DetailViewViewModel() }
}