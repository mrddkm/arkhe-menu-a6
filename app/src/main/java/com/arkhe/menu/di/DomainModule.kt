package com.arkhe.menu.di

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
import com.arkhe.menu.domain.usecase.product.SyncProductsUseCase
import com.arkhe.menu.domain.usecase.profile.GetProfileUseCase
import com.arkhe.menu.domain.usecase.profile.GetProfilesUseCase
import com.arkhe.menu.domain.usecase.profile.ProfileUseCases
import com.arkhe.menu.domain.usecase.profile.RefreshProfilesUseCase
import com.arkhe.menu.domain.usecase.profile.SyncProfilesUseCase
import com.arkhe.menu.domain.usecase.theme.GetCurrentThemeUseCase
import com.arkhe.menu.domain.usecase.theme.SetThemeUseCase
import com.arkhe.menu.domain.usecase.theme.ThemeUseCases
import org.koin.dsl.module

val domainModule = module {

    single { GetCurrentThemeUseCase(get()) }
    single { SetThemeUseCase(get()) }

    single {
        ThemeUseCases(
            getCurrentTheme = get(),
            setTheme = get()
        )
    }

    single { GetProfilesUseCase(get()) }
    single { GetProfileUseCase(get()) }
    single { RefreshProfilesUseCase(get()) }
    single { SyncProfilesUseCase(get()) }

    single {
        ProfileUseCases(
            getProfiles = get(),
            getProfile = get(),
            refreshProfiles = get(),
            syncProfiles = get()
        )
    }

    single { GetCategoriesUseCase(get()) }
    single { GetCategoryUseCase(get()) }
    single { RefreshCategoriesUseCase(get()) }

    single {
        CategoryUseCases(
            getCategories = get(),
            getCategory = get(),
            refreshCategories = get()
        )
    }

    single { GetProductsUseCase(get()) }
    single { GetProductUseCase(get()) }
    single { GetProductsByCategoryUseCase(get()) }
    single { GetProductsByNamePrefixUseCase(get()) }
    single { GetProductGroupsUseCase(get()) }
    single { RefreshProductsUseCase(get()) }
    single { SyncProductsUseCase(get()) }

    single {
        ProductUseCases(
            getProducts = get(),
            getProduct = get(),
            getProductsByCategory = get(),
            getProductsByNamePrefix = get(),
            getProductGroups = get(),
            refreshProducts = get(),
            syncProducts = get()
        )
    }
}