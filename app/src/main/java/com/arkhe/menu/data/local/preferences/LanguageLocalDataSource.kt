package com.arkhe.menu.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.arkhe.menu.domain.model.Languages
import com.arkhe.menu.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LanguageLocalDataSource(private val context: Context) {

    val selectedLanguageCode: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[Constants.Database.LANGUAGE_CODE_KEY] ?: Languages.ENGLISH.code
        }

    suspend fun setLanguage(languageCode: String) {
        context.dataStore.edit { preferences ->
            preferences[Constants.Database.LANGUAGE_CODE_KEY] = languageCode
        }
    }
}