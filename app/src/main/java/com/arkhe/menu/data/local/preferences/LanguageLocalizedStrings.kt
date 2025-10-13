package com.arkhe.menu.data.local.preferences

import android.content.Context
import com.arkhe.menu.R
import com.arkhe.menu.utils.LanguageManager

object LanguageLocalizedStrings {

    fun getLocalizedStringsMap(
        appContext: Context,
        languageCode: String
    ): Map<String, String> {
        return mapOf(
            Lang.APP_NAME to LanguageManager.getLocalizedString(
                appContext,
                R.string.app_name,
                languageCode
            ),
            Lang.PROFILE_SETTINGS to LanguageManager.getLocalizedString(
                appContext,
                R.string.profile_settings,
                languageCode
            ),
            Lang.PERSONAL_INFO to LanguageManager.getLocalizedString(
                appContext,
                R.string.personal_info,
                languageCode
            ),
            Lang.PERSONAL_INFO_DESC to LanguageManager.getLocalizedString(
                appContext,
                R.string.personal_info_desc,
                languageCode
            ),
            Lang.SIGN_IN_AND_SECURITY to LanguageManager.getLocalizedString(
                appContext,
                R.string.sign_in_and_security,
                languageCode
            ),
            Lang.SIGN_IN_AND_SECURITY_DESC to LanguageManager.getLocalizedString(
                appContext,
                R.string.sign_in_and_security_desc,
                languageCode
            ),
            Lang.DEVICES to LanguageManager.getLocalizedString(
                appContext,
                R.string.devices,
                languageCode
            ),
            Lang.DEVICES_DESC to LanguageManager.getLocalizedString(
                appContext,
                R.string.devices_desc,
                languageCode
            ),
            Lang.LANGUAGE to LanguageManager.getLocalizedString(
                appContext,
                R.string.language,
                languageCode
            ),
            Lang.THEME to LanguageManager.getLocalizedString(
                appContext,
                R.string.theme,
                languageCode
            ),
            Lang.ABOUT to LanguageManager.getLocalizedString(
                appContext,
                R.string.about,
                languageCode
            ),
            Lang.ABOUT_DESC to LanguageManager.getLocalizedString(
                appContext,
                R.string.about_desc,
                languageCode
            ),
            Lang.PRIVACY_POLICY to LanguageManager.getLocalizedString(
                appContext,
                R.string.privacy_policy,
                languageCode
            ),
            Lang.TERMS_OF_SERVICE to LanguageManager.getLocalizedString(
                appContext,
                R.string.terms_of_service,
                languageCode
            ),
            Lang.CHANGING_LANGUAGE to LanguageManager.getLocalizedString(
                appContext,
                R.string.changing_language,
                languageCode
            ),
            Lang.PLEASE_WAIT to LanguageManager.getLocalizedString(
                appContext,
                R.string.please_wait,
                languageCode
            ),
            /*Auth*/
            Lang.ACTIVATION to LanguageManager.getLocalizedString(
                appContext,
                R.string.activation,
                languageCode
            ),
        )
    }
}

object Lang {
    const val APP_NAME = "app_name"
    const val PROFILE_SETTINGS = "profile_settings"
    const val PERSONAL_INFO = "personal_info"
    const val PERSONAL_INFO_DESC = "personal_info_desc"
    const val SIGN_IN_AND_SECURITY = "sign_in_and_security"
    const val SIGN_IN_AND_SECURITY_DESC = "sign_in_and_security_desc"
    const val DEVICES = "devices"
    const val DEVICES_DESC = "devices_desc"
    const val LANGUAGE = "language"
    const val THEME = "theme"
    const val ABOUT = "about"
    const val ABOUT_DESC = "about_desc"
    const val PRIVACY_POLICY = "privacy_policy"
    const val TERMS_OF_SERVICE = "terms_of_service"
    const val CHANGING_LANGUAGE = "changing_language"
    const val PLEASE_WAIT = "please_wait"
    /*Auth*/
    const val ACTIVATION = "activation"
}