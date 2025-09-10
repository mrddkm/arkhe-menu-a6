package com.arkhe.menu.data.local.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.arkhe.menu.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SessionManager(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private const val TAG = "SessionManager"
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

    @Suppress("Unused")
    val userId: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    @Suppress("Unused")
    val username: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USERNAME_KEY]
    }

    suspend fun saveSession(token: String, userId: String? = null, username: String? = null) {
        dataStore.edit { preferences ->
            preferences[SESSION_TOKEN_KEY] = token
            userId?.let { preferences[USER_ID_KEY] = it }
            username?.let { preferences[USERNAME_KEY] = it }
        }
    }

    @Suppress("Unused")
    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(SESSION_TOKEN_KEY)
            preferences.remove(USER_ID_KEY)
            preferences.remove(USERNAME_KEY)
        }
    }

    @Suppress("Unused")
    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SESSION_TOKEN_KEY] != null
    }

    /**
     * Ensure token is available and return it
     * This method guarantees a valid token is returned
     */
    suspend fun ensureTokenAvailable(): String {
        return try {
            val currentToken = sessionToken.first()

            if (currentToken.isNullOrEmpty()) {
                val defaultToken = Constants.Simulation.TOKEN
                saveSession(defaultToken)
                Log.d(TAG, "🔑 Default token ensured: $defaultToken")
                defaultToken
            } else {
                Log.d(TAG, "🔑 Using existing token: $currentToken")
                currentToken
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error ensuring token: ${e.message}")
            // Return default token as fallback
            Constants.Simulation.TOKEN
        }
    }

    /**
     * Get current token synchronously
     * Returns null if no token is available
     */
    fun getCurrentTokenSync(): String? {
        return try {
            sessionToken.value
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error getting token sync: ${e.message}")
            null
        }
    }

    /**
     * Get current token synchronously with fallback to default
     * Always returns a valid token
     */
    fun getCurrentTokenSyncOrDefault(): String {
        return getCurrentTokenSync() ?: Constants.Simulation.TOKEN
    }

    /**
     * Initialize token synchronously (for app startup)
     */
    fun initializeTokenSync() {
        try {
            runBlocking {
                ensureTokenAvailable()
            }
            Log.d(TAG, "✅ Token initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error initializing token: ${e.message}")
        }
    }

    /**
     * Refresh token (save new token)
     */
    suspend fun refreshToken(newToken: String) {
        try {
            saveSession(newToken)
            Log.d(TAG, "🔄 Token refreshed: $newToken")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error refreshing token: ${e.message}")
        }
    }

    /**
     * Check if token is available and valid
     */
    fun hasValidToken(): Boolean {
        val token = getCurrentTokenSync()
        val isValid = !token.isNullOrEmpty()
        Log.d(TAG, "🔍 Token validation: $isValid")
        return isValid
    }

    /**
     * Get token with automatic fallback for API calls
     * This is the main method ViewModels should use
     */
    suspend fun getTokenForApiCall(): String {
        return ensureTokenAvailable()
    }
}