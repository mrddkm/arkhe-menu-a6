package com.arkhe.menu.utils

import android.util.Base64
import java.security.MessageDigest

object CryptoUtils {
    fun hashPassword(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")

        val hashedBytes = digest.digest(input.toByteArray(Charsets.UTF_8))

        return Base64.encodeToString(
            hashedBytes,
            Base64.NO_WRAP
        )
    }
}