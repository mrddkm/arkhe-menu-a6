package com.arkhe.menu.di

import android.util.Log
import com.arkhe.menu.presentation.viewmodel.CategoryViewModel
import com.arkhe.menu.presentation.viewmodel.MainViewModel
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import com.arkhe.menu.presentation.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@Suppress("DEPRECATION")
val appModule = module {

    Log.d("AppModule", "🏗️ Initializing App Module (ViewModels)...")

    viewModel {
        Log.d("AppModule", "Creating MainViewModel")
        MainViewModel(get())
    }

    viewModel {
        Log.d("DI", "Creating ProfileViewModel")
        ProfileViewModel(get(), get())
    }

    /*✅ CategoryViewModel with explicit dependency validation*/
    viewModel {
        Log.d("DI", "🎯 Creating CategoryViewModel")

        // Explicit dependency retrieval with validation
        val categoryUseCases = try {
            get<com.arkhe.menu.domain.usecase.category.CategoryUseCases>()
        } catch (e: Exception) {
            Log.e("DI", "❌ Failed to get CategoryUseCases: ${e.message}", e)
            throw e
        }

        val sessionManager = try {
            get<com.arkhe.menu.data.local.preferences.SessionManager>()
        } catch (e: Exception) {
            Log.e("DI", "❌ Failed to get SessionManager: ${e.message}", e)
            throw e
        }

        Log.d(
            "DI",
            "✅ CategoryViewModel dependencies resolved - CategoryUseCases: $categoryUseCases, SessionManager: $sessionManager"
        )

        CategoryViewModel(categoryUseCases, sessionManager).also {
            Log.d("DI", "✅ CategoryViewModel created successfully: $it")
        }
    }

    viewModel {
        Log.d("DI", "Creating ProductViewModel")
        ProductViewModel(get(), get())
    }
}