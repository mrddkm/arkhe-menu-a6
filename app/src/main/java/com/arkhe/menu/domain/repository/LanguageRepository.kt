package com.arkhe.menu.domain.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.arkhe.menu.data.local.preferences.dataStore
import com.arkhe.menu.domain.model.Languages
import com.arkhe.menu.utils.Constants.Database.LANGUAGE_CODE_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Interface for language repository to enable mocking in previews
 */
interface ILanguageRepository {
    val selectedLanguageCode: Flow<String>
    suspend fun setLanguage(languageCode: String)
}

/**
 * Real implementation using DataStore
 */
class LanguageRepository(private val context: Context) : ILanguageRepository {

    override val selectedLanguageCode: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[LANGUAGE_CODE_KEY] ?: Languages.ENGLISH.code
        }

    override suspend fun setLanguage(languageCode: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_CODE_KEY] = languageCode
        }
    }
}

/**
 * Mock implementation for preview - doesn't require DataStore
 */
class MockLanguageRepository : ILanguageRepository {
    private val languageFlow = MutableStateFlow(Languages.ENGLISH.code)

    override val selectedLanguageCode: Flow<String> = languageFlow

    override suspend fun setLanguage(languageCode: String) {
        languageFlow.value = languageCode
    }
}