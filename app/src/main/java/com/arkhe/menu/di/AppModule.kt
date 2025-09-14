package com.arkhe.menu.di

import com.arkhe.menu.presentation.viewmodel.CategoryViewModel
import com.arkhe.menu.presentation.viewmodel.MainViewModel
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import com.arkhe.menu.presentation.viewmodel.ProfileViewModel
import com.arkhe.menu.utils.ScrollStateManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@Suppress("DEPRECATION")
val appModule = module {

    single { ScrollStateManager() }

    viewModel { MainViewModel(get(), get()) }

    viewModel { ProfileViewModel(get(), get()) }

    viewModel { CategoryViewModel(get(), get()) }

    viewModel { ProductViewModel(get(), get()) }
}