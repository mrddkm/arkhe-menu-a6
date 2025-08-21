package com.arkhe.menu.di

import com.arkhe.menu.presentation.viewmodel.MainViewModel
import com.arkhe.menu.presentation.viewmodel.receipt.CreateReceiptViewModel
import com.arkhe.menu.presentation.viewmodel.receipt.ReceiptListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@Suppress("DEPRECATION")
val appModule = module {
    // ViewModels
    viewModel { MainViewModel(get()) }
    viewModel { CreateReceiptViewModel(get()) }
    viewModel { ReceiptListViewModel(get()) }
    viewModel { DocsViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}