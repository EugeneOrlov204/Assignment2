package com.shpp.eorlov.assignment2.di

import com.shpp.eorlov.assignment2.SharedViewModel
import com.shpp.eorlov.assignment2.dialogfragment.ContactDialogFragmentViewModel
import com.shpp.eorlov.assignment2.ui.detailview.DetailViewViewModel
import com.shpp.eorlov.assignment2.ui.fragment.FragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModel: Module = module {
    viewModel { FragmentViewModel() }
    viewModel { ContactDialogFragmentViewModel() }
    viewModel { SharedViewModel() }
    viewModel { DetailViewViewModel() }
}