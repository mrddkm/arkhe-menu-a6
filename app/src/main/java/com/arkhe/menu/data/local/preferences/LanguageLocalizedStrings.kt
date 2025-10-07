package com.arkhe.menu.data.local.preferences

import android.app.Application
import com.arkhe.menu.R

object LanguageLocalizedStrings {

    fun getLocalizedStringsMap(
        application: Application,
        languageCode: String
    ): Map<String, String> {
        val context = application.applicationContext
        return mapOf(
            "app_name" to LanguageManager.getLocalizedString(
                context,
                R.string.app_name,
                languageCode
            ),
            "client_short_name" to LanguageManager.getLocalizedString(
                context,
                R.string.client_short_name,
                languageCode
            ),
            "profile" to LanguageManager.getLocalizedString(
                context,
                R.string.profile,
                languageCode
            ),
            "settings" to LanguageManager.getLocalizedString(
                context,
                R.string.settings,
                languageCode
            ),
            "profile_settings" to LanguageManager.getLocalizedString(
                context,
                R.string.profile_settings,
                languageCode
            ),
        )
    }
}