package com.arkhe.menu.di

import com.arkhe.menu.domain.repository.ILanguageRepository
import com.arkhe.menu.domain.repository.MockLanguageRepository
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Preview module - uses mock implementation without DataStore
 */
@Suppress("DEPRECATION")
val previewModule = module {
    single<ILanguageRepository> { MockLanguageRepository() }
    viewModel { LanguageViewModel(androidContext(), get()) }
}