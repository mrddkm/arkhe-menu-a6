package com.arkhe.menu.domain.usecase.profile

data class ProfileUseCases(
    val getProfiles: GetProfilesUseCase,
    val getProfile: GetProfileUseCase,
    val refreshProfiles: RefreshProfilesUseCase
)