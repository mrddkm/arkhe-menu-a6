@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arkhe.menu.presentation.screen.MainScreen
import com.arkhe.menu.presentation.screen.receipt.CreateReceiptScreen
import com.arkhe.menu.presentation.screen.receipt.ReceiptListScreen

@Composable
fun TripkeunNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.MAIN
    ) {
        composable(NavigationRoute.MAIN) {
            MainScreen(navController = navController)
        }

        composable(NavigationRoute.CREATE_RECEIPT) {
            CreateReceiptScreen(navController = navController)
        }

        composable(NavigationRoute.RECEIPT_LIST) {
            ReceiptListScreen(navController = navController)
        }
    }
}