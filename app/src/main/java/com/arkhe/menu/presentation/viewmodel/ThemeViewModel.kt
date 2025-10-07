package com.arkhe.menu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.domain.model.ThemeModels
import com.arkhe.menu.domain.usecase.theme.GetCurrentThemeUseCase
import com.arkhe.menu.domain.usecase.theme.SetThemeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    getCurrentThemeUseCase: GetCurrentThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel() {
    val currentTheme: StateFlow<ThemeModels> = getCurrentThemeUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ThemeModels.SYSTEM
        )

    fun setTheme(themeMode: ThemeModels) {
        viewModelScope.launch {
            setThemeUseCase(themeMode)
        }
    }
}