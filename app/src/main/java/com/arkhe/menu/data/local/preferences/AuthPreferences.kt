package com.arkhe.menu.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.arkhe.menu.utils.Constants.Database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Manages authentication-related flags and simple local states.
 * Uses centralized constants from Constants.Database
 * Example:
 *   val isActivated = authPreferences.isActivated()
 *   authPreferences.setSignedIn(true)
 */
class AuthPreferences(private val context: Context) {

    // ---------- Flow (Reactive state) ----------
    val isActivatedFlow: Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[Database.KEY_IS_ACTIVATED] ?: false }

    val isSignedInFlow: Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[Database.KEY_IS_SIGNED_IN] ?: false }

    val pinAttemptsFlow: Flow<Int> =
        context.dataStore.data.map { prefs -> prefs[Database.KEY_PIN_ATTEMPTS] ?: 0 }

    // ---------- Update / Write ----------
    suspend fun setActivated(value: Boolean) {
        context.dataStore.edit { prefs -> prefs[Database.KEY_IS_ACTIVATED] = value }
    }

    suspend fun setSignedIn(value: Boolean) {
        context.dataStore.edit { prefs -> prefs[Database.KEY_IS_SIGNED_IN] = value }
    }

    suspend fun setPinAttempts(value: Int) {
        context.dataStore.edit { prefs -> prefs[Database.KEY_PIN_ATTEMPTS] = value }
    }

    suspend fun resetPinAttempts() {
        setPinAttempts(0)
    }

    suspend fun isActivated(): Boolean = isActivatedFlow.first()
    suspend fun isSignedIn(): Boolean = isSignedInFlow.first()
    suspend fun getPinAttempts(): Int = pinAttemptsFlow.first()

    suspend fun deactivatedAuthState() {
        context.dataStore.edit { prefs ->
            prefs[Database.KEY_IS_ACTIVATED] = false
            prefs[Database.KEY_IS_SIGNED_IN] = false
            prefs[Database.KEY_PIN_ATTEMPTS] = 0
        }
    }

    suspend fun signedOutAuthState() {
        context.dataStore.edit { prefs ->
            prefs[Database.KEY_IS_SIGNED_IN] = false
            prefs[Database.KEY_PIN_ATTEMPTS] = 0
        }
    }
}
