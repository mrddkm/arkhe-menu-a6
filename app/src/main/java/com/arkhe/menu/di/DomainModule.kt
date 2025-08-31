package com.arkhe.menu.di

import com.arkhe.menu.domain.usecase.category.CategoryUseCases
import com.arkhe.menu.domain.usecase.category.GetCategoriesUseCase
import com.arkhe.menu.domain.usecase.category.GetCategoryUseCase
import com.arkhe.menu.domain.usecase.category.RefreshCategoriesUseCase
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

    /*Profiles Use Cases Bundle*/
    factory {
        ProfileUseCases(
            getProfiles = get(),
            getProfile = get(),
            refreshProfiles = get()
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
}