@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.utils

object Constants {
    const val URL_BASE = "https://script.google.com/macros/s/AKfycbxzU5sK4C50geQL1-PWtFm8NeQTErOcY0QGw8XvMfFUOTPKgAYYBZO8p5UuAMVeJ1PCQw/exec"
    const val URL_HOST = "script.google.com"
    const val PARAMETER_KEY = "action"
    const val PARAMETER_VALUE_PROFILES = "profiles"
    const val HTTP_HEADER_USER_AGENT = "arkhe/dvl/1.0.0"
    const val DATABASE_NAME = "tripkeun_database"
    const val DATABASE_VERSION = 1

    const val DATASTORE_NAME = "tripkeun_settings"

    const val API_BASE_URL = "https://api.tripkeun.com/v1"
    const val API_TIMEOUT = 30000L

    const val DATE_FORMAT = "dd MMM yyyy"
    const val DATETIME_FORMAT = "dd MMM yyyy, HH:mm"
    const val ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

    const val CURRENCY_CODE = "IDR"
    const val TAX_RATE = 0.1 // 10%

    const val MAX_RECEIPT_ITEMS = 50
    const val MIN_CUSTOMER_NAME_LENGTH = 2

    object UserRoles {
        const val SUPERUSER = "superuser"
        const val FAGA = "faga"
        const val MCC = "mcc"
        const val BROD = "brod"
        const val PRESDIR = "presdir"
        const val UNSPECIFIED = "unspecified"
    }

    object SharedPrefsKeys {
        const val USER_ROLE = "user_role"
        const val AUTH_TOKEN = "auth_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val USER_ID = "user_id"
        const val USER_NAME = "user_name"
    }
}