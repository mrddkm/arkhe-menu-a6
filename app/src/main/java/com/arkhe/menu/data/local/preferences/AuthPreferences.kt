package com.arkhe.menu.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.arkhe.menu.utils.Constants.Database.KEY_IS_ACTIVATED
import com.arkhe.menu.utils.Constants.Database.KEY_IS_SIGNED_IN
import com.arkhe.menu.utils.Constants.Database.KEY_PIN_ATTEMPTS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Manages authentication-related flags and simple local states.
 * Example usage:
 *   val isActivated = authPreferences.isActivated()
 *   authPreferences.setSignedIn(true)
 */

class AuthPreferences(private val context: Context) {

    suspend fun setActivated(value: Boolean) {
        context.dataStore.edit { prefs -> prefs[KEY_IS_ACTIVATED] = value }
    }

    fun isActivatedFlow(): Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[KEY_IS_ACTIVATED] ?: false }

    suspend fun isActivated(): Boolean =
        isActivatedFlow().first()

    suspend fun setSignedIn(value: Boolean) {
        context.dataStore.edit { prefs -> prefs[KEY_IS_SIGNED_IN] = value }
    }

    fun isSignedInFlow(): Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[KEY_IS_SIGNED_IN] ?: false }

    suspend fun isSignedIn(): Boolean =
        isSignedInFlow().first()

    suspend fun setPinAttempts(value: Int) {
        context.dataStore.edit { prefs -> prefs[KEY_PIN_ATTEMPTS] = value }
    }

    fun getPinAttemptsFlow(): Flow<Int> =
        context.dataStore.data.map { prefs -> prefs[KEY_PIN_ATTEMPTS] ?: 0 }

    suspend fun getPinAttempts(): Int =
        getPinAttemptsFlow().first()

    suspend fun resetPinAttempts() {
        setPinAttempts(0)
    }
}
