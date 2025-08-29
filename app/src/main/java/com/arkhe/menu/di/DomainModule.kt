package com.arkhe.menu.di

import com.arkhe.menu.domain.usecase.profile.GetProfileUseCase
import com.arkhe.menu.domain.usecase.profile.GetProfilesUseCase
import com.arkhe.menu.domain.usecase.profile.ProfileUseCases
import com.arkhe.menu.domain.usecase.profile.RefreshProfilesUseCase
import org.koin.dsl.module

val domainModule = module {

    /*Use Cases*/
    factory { GetProfilesUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { RefreshProfilesUseCase(get()) }

    /*Use Cases Bundle*/
    factory {
        ProfileUseCases(
            getProfiles = get(),
            getProfile = get(),
            refreshProfiles = get()
        )
    }
}