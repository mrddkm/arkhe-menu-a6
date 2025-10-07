package com.arkhe.menu.domain.usecase.theme

import com.arkhe.menu.domain.model.ThemeModels
import com.arkhe.menu.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentThemeUseCase(
    private val repository: ThemeRepository
) {
    operator fun invoke(): Flow<ThemeModels> = repository.getThemeMode()
}

class SetThemeUseCase(
    private val repository: ThemeRepository
) {
    suspend operator fun invoke(themeMode: ThemeModels) {
        repository.setThemeMode(themeMode)
    }
}

data class ThemeUseCases(
    val getCurrentTheme: GetCurrentThemeUseCase,
    val setTheme: SetThemeUseCase
)