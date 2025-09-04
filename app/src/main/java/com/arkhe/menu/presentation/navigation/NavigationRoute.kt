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

    /*Tripkeun*/

    /*Activity*/

    /*Helper functions for navigation with arguments*/
    fun categoryDetail(): String = "category_detail"
}