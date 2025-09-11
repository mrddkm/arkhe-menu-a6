package com.arkhe.menu

import android.app.Application
import android.util.Log
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TripkeunApplication : Application() {

    companion object {
        private const val TAG = "TripkeunApplication"
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "🚀 TripkeunApplication starting...")

        try {
            startKoin {
                // ✅ Enable detailed logging for debugging
                if (BuildConfig.DEBUG) {
                    androidLogger(Level.DEBUG)
                    Log.d(TAG, "📝 Koin debug logging enabled")
                } else {
                    androidLogger(Level.ERROR)
                }

                androidContext(this@TripkeunApplication)

                // ✅ Load modules in CORRECT dependency order
                Log.d(TAG, "📦 Loading modules in dependency order...")

                // 1. First: Data layer (Repositories, DAOs, APIs)
                // 2. Second: Domain layer (UseCases)
                // 3. Last: Presentation layer (ViewModels)
                Log.d(TAG, "📦 Loading dataModule...")
                Log.d(TAG, "📦 Loading domainModule...")
                Log.d(TAG, "📦 Loading appModule...")
                modules(dataModule, domainModule, appModule)

                Log.d(TAG, "✅ All modules loaded successfully")
            }

            // ✅ Validate critical dependencies after DI setup
            validateCriticalDependencies()

            Log.d(TAG, "🎉 TripkeunApplication initialization completed successfully")

        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to initialize application: ${e.message}", e)

            // ✅ In production, you might want to show a user-friendly error
            // or implement fallback initialization
            if (BuildConfig.DEBUG) {
                throw e  // Crash in debug to catch issues early
            } else {
                // Log error but don't crash in production
                Log.e(
                    TAG,
                    "💥 Application failed to initialize properly, some features may not work"
                )
            }
        }
    }

    /**
     * Validates that critical dependencies are properly injected
     * Helps catch DI issues early in the application lifecycle
     */
    private fun validateCriticalDependencies() {
        Log.d(TAG, "🔍 Validating critical dependencies...")

        try {
            val koin = org.koin.core.context.GlobalContext.get()

            // ✅ Validate SessionManager
            val sessionManager = koin.get<com.arkhe.menu.data.local.preferences.SessionManager>()
            Log.d(TAG, "✅ SessionManager validated: $sessionManager")

            // ✅ Validate CategoryRepository
            val categoryRepo = koin.get<com.arkhe.menu.domain.repository.CategoryRepository>()
            Log.d(TAG, "✅ CategoryRepository validated: $categoryRepo")

            // ✅ Validate CategoryUseCases and its nested components
            val categoryUseCases =
                koin.get<com.arkhe.menu.domain.usecase.category.CategoryUseCases>()
            Log.d(TAG, "✅ CategoryUseCases validated: $categoryUseCases")

            // Validate nested use cases
            val getCategories = categoryUseCases.getCategories
            val getCategory = categoryUseCases.getCategory
            val refreshCategories = categoryUseCases.refreshCategories

            Log.d(TAG, "✅ GetCategoriesUseCase: $getCategories")
            Log.d(TAG, "✅ GetCategoryUseCase: $getCategory")
            Log.d(TAG, "✅ RefreshCategoriesUseCase: $refreshCategories")

            // ✅ Try to create CategoryViewModel (this will fail if dependencies are wrong)
            val categoryViewModel =
                koin.get<com.arkhe.menu.presentation.viewmodel.CategoryViewModel>()
            Log.d(TAG, "✅ CategoryViewModel creation test passed: $categoryViewModel")

            Log.d(TAG, "🎯 All critical dependencies validated successfully")

        } catch (e: Exception) {
            Log.e(TAG, "❌ Critical dependency validation failed: ${e.message}", e)

            // ✅ Additional debugging info
            logDependencyDiagnostics()

            if (BuildConfig.DEBUG) {
                throw e  // Fail fast in debug mode
            } else {
                // In production, log but continue (app might work with reduced functionality)
                Log.w(TAG, "⚠️ App will continue but some features might not work properly")
            }
        }
    }

    /**
     * Provides detailed diagnostic information when dependency validation fails
     */
    private fun logDependencyDiagnostics() {
        Log.d(TAG, "🔧 === DEPENDENCY DIAGNOSTICS ===")

        try {
            val koin = org.koin.core.context.GlobalContext.get()

            // Check if basic services exist
            Log.d(TAG, "🔧 Checking individual dependencies...")

            try {
                koin.get<com.arkhe.menu.data.local.preferences.SessionManager>()
                Log.d(TAG, "🔧 ✅ SessionManager: Available")
            } catch (e: Exception) {
                Log.d(TAG, "🔧 ❌ SessionManager: ${e.message}")
            }

            try {
                koin.get<com.arkhe.menu.domain.repository.CategoryRepository>()
                Log.d(TAG, "🔧 ✅ CategoryRepository: Available")
            } catch (e: Exception) {
                Log.d(TAG, "🔧 ❌ CategoryRepository: ${e.message}")
            }

            try {
                koin.get<com.arkhe.menu.domain.usecase.category.GetCategoriesUseCase>()
                Log.d(TAG, "🔧 ✅ GetCategoriesUseCase: Available")
            } catch (e: Exception) {
                Log.d(TAG, "🔧 ❌ GetCategoriesUseCase: ${e.message}")
            }

            try {
                koin.get<com.arkhe.menu.domain.usecase.category.CategoryUseCases>()
                Log.d(TAG, "🔧 ✅ CategoryUseCases: Available")
            } catch (e: Exception) {
                Log.d(TAG, "🔧 ❌ CategoryUseCases: ${e.message}")
            }

        } catch (e: Exception) {
            Log.e(TAG, "🔧 Failed to run diagnostics: ${e.message}")
        }

        Log.d(TAG, "🔧 === END DIAGNOSTICS ===")
    }
}
