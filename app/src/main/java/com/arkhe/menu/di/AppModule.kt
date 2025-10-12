@file:Suppress("DEPRECATION")

package com.arkhe.menu.di

import com.arkhe.menu.presentation.viewmodel.AuthViewModel
import com.arkhe.menu.presentation.viewmodel.CategoryViewModel
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import com.arkhe.menu.presentation.viewmodel.MainViewModel
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import com.arkhe.menu.presentation.viewmodel.ProfileViewModel
import com.arkhe.menu.presentation.viewmodel.ThemeViewModel
import com.arkhe.menu.utils.ScrollStateManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { ScrollStateManager() }

    viewModel { AuthViewModel(get()) }

    viewModel { MainViewModel(get(), get()) }

    viewModel { ThemeViewModel(get(), get()) }

    viewModel { LanguageViewModel(androidContext(), get()) }

    viewModel { ProfileViewModel(get(), get()) }

    viewModel { CategoryViewModel(get(), get()) }

    viewModel { ProductViewModel(get(), get()) }
}