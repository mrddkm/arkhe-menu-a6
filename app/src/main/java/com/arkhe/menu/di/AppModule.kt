package com.arkhe.menu.di

import com.arkhe.menu.presentation.viewmodel.MainViewModel
import com.arkhe.menu.presentation.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@Suppress("DEPRECATION")
val appModule = module {
    viewModel { MainViewModel() }
    viewModel { ProfileViewModel(get(), get()) }
}