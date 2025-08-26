package com.arkhe.menu.domain.usecase

data class ProfileUseCases(
    val getProfiles: GetProfilesUseCase,
    val getProfile: GetProfileUseCase,
    val refreshProfiles: RefreshProfilesUseCase
)