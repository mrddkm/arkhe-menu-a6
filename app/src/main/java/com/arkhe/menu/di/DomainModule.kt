package com.arkhe.menu.di

import android.util.Log
import com.arkhe.menu.domain.usecase.category.CategoryUseCases
import com.arkhe.menu.domain.usecase.category.GetCategoriesUseCase
import com.arkhe.menu.domain.usecase.category.GetCategoryUseCase
import com.arkhe.menu.domain.usecase.category.RefreshCategoriesUseCase
import com.arkhe.menu.domain.usecase.product.GetProductGroupsUseCase
import com.arkhe.menu.domain.usecase.product.GetProductUseCase
import com.arkhe.menu.domain.usecase.product.GetProductsByCategoryUseCase
import com.arkhe.menu.domain.usecase.product.GetProductsByNamePrefixUseCase
import com.arkhe.menu.domain.usecase.product.GetProductsUseCase
import com.arkhe.menu.domain.usecase.product.ProductUseCases
import com.arkhe.menu.domain.usecase.product.RefreshProductsUseCase
import com.arkhe.menu.domain.usecase.profile.GetProfileUseCase
import com.arkhe.menu.domain.usecase.profile.GetProfilesUseCase
import com.arkhe.menu.domain.usecase.profile.ProfileUseCases
import com.arkhe.menu.domain.usecase.profile.RefreshProfilesUseCase
import com.arkhe.menu.domain.usecase.profile.SyncProfilesUseCase
import org.koin.dsl.module

val domainModule = module {

    Log.d("DomainModule", "🏗️ Initializing Domain Module...")

    /*✅ Profiles Use Cases - Changed to single for consistency*/
    single {
        Log.d("DI", "Creating GetProfilesUseCase")
        GetProfilesUseCase(get())
    }
    single {
        Log.d("DI", "Creating GetProfileUseCase")
        GetProfileUseCase(get())
    }
    single {
        Log.d("DI", "Creating RefreshProfilesUseCase")
        RefreshProfilesUseCase(get())
    }
    single {
        Log.d("DI", "Creating SyncProfilesUseCase")
        SyncProfilesUseCase(get())
    }

    /*✅ Profiles Use Cases Bundle*/
    single {
        Log.d("DI", "Creating ProfileUseCases bundle")
        ProfileUseCases(
            getProfiles = get(),
            getProfile = get(),
            refreshProfiles = get(),
            syncProfiles = get()
        )
    }

    /*✅ Category Use Cases - Changed to single for consistency*/
    single {
        Log.d(
            "DI",
            "Creating GetCategoriesUseCase with repository: ${get<com.arkhe.menu.domain.repository.CategoryRepository>()}"
        )
        GetCategoriesUseCase(get())
    }
    single {
        Log.d("DI", "Creating GetCategoryUseCase")
        GetCategoryUseCase(get())
    }
    single {
        Log.d("DI", "Creating RefreshCategoriesUseCase")
        RefreshCategoriesUseCase(get())
    }

    /*✅ Category Use Cases Bundle with explicit validation*/
    single {
        Log.d("DI", "Creating CategoryUseCases bundle")
        val getCategories = get<GetCategoriesUseCase>()
        val getCategory = get<GetCategoryUseCase>()
        val refreshCategories = get<RefreshCategoriesUseCase>()

        Log.d(
            "DI",
            "CategoryUseCases dependencies - getCategories: $getCategories, getCategory: $getCategory, refreshCategories: $refreshCategories"
        )

        CategoryUseCases(
            getCategories = getCategories,
            getCategory = getCategory,
            refreshCategories = refreshCategories
        ).also {
            Log.d("DI", "✅ CategoryUseCases created successfully: $it")
        }
    }

    /*✅ Product Use Cases - Changed to single for consistency*/
    single {
        Log.d("DI", "Creating GetProductsUseCase")
        GetProductsUseCase(get())
    }
    single {
        Log.d("DI", "Creating GetProductUseCase")
        GetProductUseCase(get())
    }
    single {
        Log.d("DI", "Creating GetProductsByCategoryUseCase")
        GetProductsByCategoryUseCase(get())
    }
    single {
        Log.d("DI", "Creating GetProductsByNamePrefixUseCase")
        GetProductsByNamePrefixUseCase(get())
    }
    single {
        Log.d("DI", "Creating GetProductGroupsUseCase")
        GetProductGroupsUseCase(get())
    }
    single {
        Log.d("DI", "Creating RefreshProductsUseCase")
        RefreshProductsUseCase(get())
    }

    /*✅ Product Use Cases Bundle*/
    single {
        Log.d("DI", "Creating ProductUseCases bundle")
        ProductUseCases(
            getProducts = get(),
            getProduct = get(),
            getProductsByCategory = get(),
            getProductsByNamePrefix = get(),
            getProductGroups = get(),
            refreshProducts = get()
        )
    }
}