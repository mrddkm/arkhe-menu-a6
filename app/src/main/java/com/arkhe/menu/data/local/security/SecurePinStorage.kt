@file:Suppress("DEPRECATION")

package com.arkhe.menu.data.local.security

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.MessageDigest

class SecurePinStorage(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_pin_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_PIN = "user_pin"
        private const val KEY_ATTEMPTS = "pin_attempts"
    }

    fun saveHashedPin(pin: String) {
        val hashed = pin.sha256()
        prefs.edit().apply {
            putString(KEY_PIN, hashed)
            putInt(KEY_ATTEMPTS, 0)
            apply()
        }
    }

    fun checkPin(pinInput: String): Boolean {
        val stored = prefs.getString(KEY_PIN, null)
        return stored != null && stored == pinInput.sha256()
    }

    fun incrementAttempts() {
        val attempts = prefs.getInt(KEY_ATTEMPTS, 0) + 1
        prefs.edit { putInt(KEY_ATTEMPTS, attempts) }
    }

    fun resetAttempts() {
        prefs.edit { putInt(KEY_ATTEMPTS, 0) }
    }

    fun getAttempts(): Int = prefs.getInt(KEY_ATTEMPTS, 0)

    private fun String.sha256(): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
