package com.arkhe.menu.presentation.screen.institution

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkhe.menu.utils.ScrollStateManager
import com.arkhe.menu.utils.rememberManagedScrollState

@Composable
fun TripkeunScreen(
    scrollKey: String,
    scrollStateManager: ScrollStateManager,
    onNavigateToContent: (String) -> Unit,
    topBarHeight: Dp = 0.dp,
    bottomBarHeight: Dp = 0.dp
) {
    val lazyListState = rememberManagedScrollState(
        key = scrollKey,
        scrollStateManager = scrollStateManager
    )

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = topBarHeight,
            bottom = bottomBarHeight
        )
    ) {
        item {
            TripkeunContent(onNavigateToContent = onNavigateToContent)
        }
    }
}