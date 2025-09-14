package com.arkhe.menu.presentation.screen.institution

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkhe.menu.utils.ScrollStateManager
import com.arkhe.menu.utils.rememberManagedScrollState

@Composable
fun TripkeunScreen(
    scrollKey: String,
    scrollStateManager: ScrollStateManager,
    onNavigateToContent: (String) -> Unit
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
            TripkeunContent(onNavigateToContent = onNavigateToContent)
        }
    }
}