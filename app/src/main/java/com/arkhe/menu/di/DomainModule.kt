package com.arkhe.menu.di

import com.arkhe.menu.domain.usecase.CreateReceiptUseCase
import com.arkhe.menu.domain.usecase.GetTripDataUseCase
import com.arkhe.menu.domain.usecase.GetUserRoleUseCase
import org.koin.dsl.module

val domainModule = module {
    // Use Cases
    single { GetUserRoleUseCase(get()) }
    single { GetTripDataUseCase(get()) }
    single { CreateReceiptUseCase(get()) }
}