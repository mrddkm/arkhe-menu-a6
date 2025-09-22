@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.navigation

object NavigationRoute {
    /*Main*/
    const val MAIN = "main"

    /*Docs*/
    const val PROFILE = "profile"
    const val ORGANIZATION = "organization"
    const val CUSTOMER = "customer"
    const val CATEGORIES = "categories"
    const val CATEGORY_DETAIL = "category_detail"
    const val PRODUCTS = "products"
    const val PRODUCT_DETAIL = "product_detail/{productId}/{source}"

    /*Tripkeun*/

    /*Activity*/

    /*Helper functions for navigation with arguments*/
    fun categoryDetail(): String = CATEGORY_DETAIL

    fun productDetail(productId: String, source: String): String =
        "product_detail/$productId/$source"

    fun productDetailRoute(): String = PRODUCT_DETAIL
}