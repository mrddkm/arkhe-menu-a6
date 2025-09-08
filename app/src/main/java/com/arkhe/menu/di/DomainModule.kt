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
import com.arkhe.menu.domain.usecase.profile.DownloadProfileImagesUseCase
import com.arkhe.menu.domain.usecase.profile.GetProfileImagePathUseCase
import com.arkhe.menu.domain.usecase.profile.GetProfileUseCase
import com.arkhe.menu.domain.usecase.profile.GetProfilesUseCase
import com.arkhe.menu.domain.usecase.profile.ProfileUseCases
import com.arkhe.menu.domain.usecase.profile.RefreshProfilesUseCase
import org.koin.dsl.module

val domainModule = module {

    /*Profiles Use Cases*/
    factory { GetProfilesUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { RefreshProfilesUseCase(get()) }
    factory { DownloadProfileImagesUseCase(get()) }
    factory { GetProfileImagePathUseCase(get()) }

    /*Profiles Use Cases Bundle*/
    factory {
        ProfileUseCases(
            getProfiles = get(),
            getProfile = get(),
            refreshProfiles = get(),
            downloadProfileImages = get(),
            getProfileImagePath = get()
        )
    }

    /*Category Use Cases*/
    factory { GetCategoriesUseCase(get()) }
    factory { GetCategoryUseCase(get()) }
    factory { RefreshCategoriesUseCase(get()) }

    /*Category Use Cases Bundle*/
    factory {
        CategoryUseCases(
            getCategories = get(),
            getCategory = get(),
            refreshCategories = get()
        )
    }

    /*Product Use Cases*/
    factory { GetProductsUseCase(get()) }
    factory { GetProductUseCase(get()) }
    factory { GetProductsByCategoryUseCase(get()) }
    factory { GetProductsByNamePrefixUseCase(get()) }
    factory { GetProductGroupsUseCase(get()) }
    factory { RefreshProductsUseCase(get()) }

    /*Product Use Cases Bundle*/
    factory {
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