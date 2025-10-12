package com.arkhe.menu.presentation.navigation

object NavigationRoute {
    /*OnBoarding*/
    const val ON_BOARDING = "on_boarding"

    /*Auth*/

    /*Main*/
    const val MAIN = "main"

    /*Docs*/
    const val DOCS = "docs"
    const val PROFILE = "profile"
    const val ORGANIZATION = "organization"
    const val CUSTOMER = "customer"
    const val CATEGORIES = "categories"
    const val CATEGORY_DETAIL = "category_detail"
    const val PRODUCTS = "products"
    const val PRODUCT_DETAIL = "product_detail/{productId}/{source}"
    const val PERSONAL_INFO_DETAIL = "personal_info_detail/{source}"
    const val SIGN_IN_SECURITY_DETAIL = "sign_in_security_detail/{source}"
    const val DEVICES_DETAIL = "devices_detail/{source}"
    const val ABOUT_DETAIL = "about_detail/{source}"
    const val PRIVACY_POLICY_DETAIL = "privacy_policy_detail/{source}"
    const val TERMS_OF_SERVICE_DETAIL = "term_of_service_detail/{source}"

    /*Tripkeun*/

    /*Activity*/

    /*Helper functions for navigation with arguments*/
    fun categoryDetail(): String = CATEGORY_DETAIL

    fun productDetail(productId: String, source: String): String =
        "product_detail/$productId/$source"

    fun personalInfoDetail(source: String): String =
        "personal_info_detail/$source"

    fun signInSecurityDetail(source: String): String =
        "sign_in_security_detail/$source"

    fun devicesDetail(source: String): String =
        "devices_detail/$source"

    fun aboutDetail(source: String): String =
        "about_detail/$source"

    fun privacyPolicyDetail(source: String): String =
        "privacy_policy_detail/$source"

    fun termOfServiceDetail(source: String): String =
        "term_of_service_detail/$source"
}