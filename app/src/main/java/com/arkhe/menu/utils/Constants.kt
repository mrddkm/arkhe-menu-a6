@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.utils

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val URL_BASE =
        "https://script.google.com/macros/s/AKfycbxzU5sK4C50geQL1-PWtFm8NeQTErOcY0QGw8XvMfFUOTPKgAYYBZO8p5UuAMVeJ1PCQw/exec"
    const val URL_HOST = "script.google.com"
    const val PARAMETER_KEY = "action"
    const val HTTP_HEADER_USER_AGENT = "arkhe/dvl/1.0.0"

    /*Action Params*/
    const val PARAMETER_VALUE_PROFILES = "profiles"
    const val PARAMETER_VALUE_CATEGORY = "productcategory"
    const val PARAMETER_VALUE_PRODUCTS = "products"

    const val DATE_FORMAT = "dd MMM yyyy"
    const val DATETIME_FORMAT = "dd MMM yyyy, HH:mm"
    const val ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

    const val MAX_RECEIPT_ITEMS = 50
    const val MIN_CUSTOMER_NAME_LENGTH = 2

    object Database {
        /*room*/
        const val DATABASE_NAME = "arkhe_menu_database"
        const val DATABASE_VERSION = 1
        const val PROFILE_TABLE = "profiles"
        const val CATEGORIES_TABLE = "categories"
        const val PRODUCT_TABLE = "products"
        /*preferences data store*/
        const val DATASTORE_NAME = "app_preferences"
        val THEME_KEY = stringPreferencesKey("theme_mode")
        val LANGUAGE_CODE_KEY = stringPreferencesKey("language_code")
    }

    object CurrentLanguage {
        const val INDONESIAN = "id"
        const val ENGLISH = "en"
    }

    object Statistics {
        const val STATISTICS_TITLE = "Statistics"
        const val STATISTICS_TOTAL = "Total"
        const val STATISTICS_READY = "Ready"
        const val STATISTICS_RESEARCH = "Research"
        const val STATISTICS_INITIATION = "Initiation"
    }

    object Simulation {
        const val TOKEN = "WWUmyoU9yWjVUsn8"
    }
}