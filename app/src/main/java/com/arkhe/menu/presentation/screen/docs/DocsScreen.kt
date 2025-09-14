package com.arkhe.menu.presentation.screen.docs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkhe.menu.utils.ScrollStateManager
import com.arkhe.menu.utils.rememberManagedScrollState

@Composable
fun DocsScreen(
    scrollKey: String,
    scrollStateManager: ScrollStateManager,
    onNavigateToProfile: () -> Unit,
    onNavigateToOrganization: () -> Unit = {},
    onNavigateToCustomer: () -> Unit = {},
    onNavigateToCategories: () -> Unit = {},
    onNavigateToProducts: () -> Unit = {}
) {
    val lazyListState = rememberManagedScrollState(
        key = scrollKey,
        scrollStateManager = scrollStateManager
    )

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            DocsContent(
                onNavigateToProfile = onNavigateToProfile,
                onNavigateToOrganization = onNavigateToOrganization,
                onNavigateToCustomer = onNavigateToCustomer,
                onNavigateToCategories = onNavigateToCategories,
                onNavigateToProducts = onNavigateToProducts
            )
        }
    }
}