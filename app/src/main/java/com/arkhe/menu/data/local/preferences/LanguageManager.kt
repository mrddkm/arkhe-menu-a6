package com.arkhe.menu.data.local.preferences

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale

object LanguageManager {

    fun getLocalizedResources(context: Context, languageCode: String): Resources {
        @Suppress("DEPRECATION") val locale = Locale(languageCode)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config).resources
    }

    fun getLocalizedString(context: Context, stringResId: Int, languageCode: String): String {
        return getLocalizedResources(context, languageCode).getString(stringResId)
    }
}