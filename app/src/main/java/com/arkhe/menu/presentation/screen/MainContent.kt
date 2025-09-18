@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.arkhe.menu.domain.model.UserRole
import com.arkhe.menu.presentation.screen.activity.ActivityScreen
import com.arkhe.menu.presentation.screen.docs.DocsScreen
import com.arkhe.menu.presentation.screen.institution.TripkeunScreen
import com.arkhe.menu.presentation.viewmodel.BottomNavItem
import com.arkhe.menu.utils.ScrollStateManager
import org.koin.compose.koinInject

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    selectedBottomNavItem: BottomNavItem,
    userRole: UserRole,
    navController: NavHostController,
    onNavigateToContent: (String) -> Unit,
    onNavigateToProfile: (() -> Unit),
    onNavigateToOrganization: (() -> Unit),
    onNavigateToCustomer: (() -> Unit),
    onNavigateToCategories: (() -> Unit),
    onNavigateToProducts: (() -> Unit),
) {
    val scrollStateManager: ScrollStateManager = koinInject()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 0.dp)
    ) {
        when (selectedBottomNavItem) {
            BottomNavItem.DOCS -> {
                DocsScreen(
                    scrollKey = "main_docs_screen",
                    scrollStateManager = scrollStateManager,
                    onNavigateToProfile = onNavigateToProfile,
                    onNavigateToOrganization = onNavigateToOrganization,
                    onNavigateToCustomer = onNavigateToCustomer,
                    onNavigateToCategories = onNavigateToCategories,
                    onNavigateToProducts = onNavigateToProducts
                )
            }

            BottomNavItem.TRIPKEUN -> {
                TripkeunScreen(
                    scrollKey = "main_tripkeun_screen",
                    scrollStateManager = scrollStateManager,
                    onNavigateToContent = onNavigateToContent
                )
            }

            BottomNavItem.ACTIVITY -> {
                ActivityScreen(
                    scrollKey = "main_activity_screen",
                    scrollStateManager = scrollStateManager,
                    userRole = userRole,
                    onNavigateToContent = onNavigateToContent
                )
            }
        }
    }
}
