package com.arkhe.menu.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SessionManager(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val SESSION_TOKEN_KEY = stringPreferencesKey("sessionToken")
        private val USER_ID_KEY = stringPreferencesKey("userId")
        private val USERNAME_KEY = stringPreferencesKey("username")
    }

    private val _sessionToken: MutableStateFlow<String?> = MutableStateFlow(null)
    val sessionToken: StateFlow<String?> = _sessionToken

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.data.map { prefs -> prefs[SESSION_TOKEN_KEY] }
                .collect { token -> _sessionToken.value = token }
        }
    }

    // Get session token
    /*    val sessionToken: Flow<String?> = dataStore.data.map { preferences ->
            preferences[SESSION_TOKEN_KEY]
        }*/

    // Get user ID
    val userId: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    // Get username
    val username: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USERNAME_KEY]
    }

    // Save session data
    suspend fun saveSession(token: String, userId: String? = null, username: String? = null) {
        dataStore.edit { preferences ->
            preferences[SESSION_TOKEN_KEY] = token
            userId?.let { preferences[USER_ID_KEY] = it }
            username?.let { preferences[USERNAME_KEY] = it }
        }
    }

    // Clear session
    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(SESSION_TOKEN_KEY)
            preferences.remove(USER_ID_KEY)
            preferences.remove(USERNAME_KEY)
        }
    }

    // Check if user is logged in
    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SESSION_TOKEN_KEY] != null
    }
}