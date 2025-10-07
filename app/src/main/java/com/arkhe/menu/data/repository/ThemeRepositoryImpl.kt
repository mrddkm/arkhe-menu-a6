package com.arkhe.menu.data.repository

import com.arkhe.menu.data.local.preferences.ThemeLocalDataSource
import com.arkhe.menu.domain.model.ThemeModels
import com.arkhe.menu.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow

class ThemeRepositoryImpl(
    private val localDataSource: ThemeLocalDataSource
) : ThemeRepository {

    override fun getThemeMode(): Flow<ThemeModels> {
        return localDataSource.getThemeMode()
    }

    override suspend fun setThemeMode(themeMode: ThemeModels) {
        localDataSource.setThemeMode(themeMode)
    }
}