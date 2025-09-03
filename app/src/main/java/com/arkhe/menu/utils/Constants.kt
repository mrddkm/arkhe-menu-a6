@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.utils

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

    const val DATABASE_NAME = "tripkeun_database"
    const val DATABASE_VERSION = 1
    const val DATASTORE_NAME = "tripkeun_settings"

    const val DATE_FORMAT = "dd MMM yyyy"
    const val DATETIME_FORMAT = "dd MMM yyyy, HH:mm"
    const val ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

    const val CURRENCY_CODE = "IDR"

    const val MAX_RECEIPT_ITEMS = 50
    const val MIN_CUSTOMER_NAME_LENGTH = 2

    object CurrentLanguage {
        const val INDONESIAN = "id"
        const val ENGLISH = "en"
    }

    object Product {
        const val PRODUCT_LABEL = "Product"
        const val STATUS_INITIATION = "initiation"
        const val STATUS_RESEARCH = "research"
        const val STATUS_READY = "ready"
    }

    object Category{
        const val CATEGORY_LABEL = "Category"
        const val STATISTICS_LABEL = "Statistics"
    }

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