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

    const val MAX_RECEIPT_ITEMS = 50
    const val MIN_CUSTOMER_NAME_LENGTH = 2

    object CurrentLanguage {
        const val INDONESIAN = "id"
        const val ENGLISH = "en"
    }

    object Product {
        const val PRODUCT_LABEL = "Product"
    }

    object Category {
        const val CATEGORY_LABEL = "Category"
        const val STATISTICS_LABEL = "Statistics"
    }

    object Simulation {
        const val TOKEN = "WWUmyoU9yWjVUsn8"
    }
}