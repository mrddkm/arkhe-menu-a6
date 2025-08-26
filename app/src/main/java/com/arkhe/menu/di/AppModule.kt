package com.arkhe.menu.di

import com.arkhe.menu.presentation.viewmodel.MainViewModel
import com.arkhe.menu.presentation.viewmodel.receipt.CreateReceiptViewModel
import com.arkhe.menu.presentation.viewmodel.receipt.ReceiptListViewModel
import com.arkhe.menu.presentation.viewmodel.docs.DocsViewModel
import com.arkhe.menu.presentation.viewmodel.docs.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@Suppress("DEPRECATION")
val appModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    /*Examples*/
    viewModel { DocsViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { CreateReceiptViewModel(get()) }
    viewModel { ReceiptListViewModel(get()) }
}