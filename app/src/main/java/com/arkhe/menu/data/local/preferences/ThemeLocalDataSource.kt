package com.arkhe.menu.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.arkhe.menu.domain.model.ThemeModels
import com.arkhe.menu.utils.Constants.Database.THEME_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeLocalDataSource(private val context: Context) {

    fun getThemeMode(): Flow<ThemeModels> = context.dataStore.data.map { preferences ->
        val themeName = preferences[THEME_KEY] ?: ThemeModels.SYSTEM.name
        ThemeModels.valueOf(themeName)
    }

    suspend fun setThemeMode(themeMode: ThemeModels) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = themeMode.name
        }
    }
}