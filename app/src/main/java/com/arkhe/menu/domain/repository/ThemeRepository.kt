package com.arkhe.menu.domain.repository

import com.arkhe.menu.domain.model.ThemeModels
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getThemeMode(): Flow<ThemeModels>
    suspend fun setThemeMode(themeMode: ThemeModels)
}