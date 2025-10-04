package com.arkhe.menu.presentation.screen.activity

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.UserRole
import com.arkhe.menu.utils.ScrollStateManager
import com.arkhe.menu.utils.rememberManagedScrollState

@Composable
fun ActivityScreen(
    scrollKey: String,
    scrollStateManager: ScrollStateManager,
    userRole: UserRole,
    onNavigateToContent: (String) -> Unit,
    topBarHeight: Dp = 0.dp,
    bottomBarHeight: Dp = 0.dp
) {
    val topBarHeightPlus = topBarHeight + 8.dp
    val bottomBarHeightPlus = bottomBarHeight + 16.dp

    val lazyListState = rememberManagedScrollState(
        key = scrollKey,
        scrollStateManager = scrollStateManager
    )

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = topBarHeightPlus,
            bottom = bottomBarHeightPlus
        )
    ) {
        item {
            ActivityContent(
                userRole = userRole,
                onNavigateToContent = onNavigateToContent
            )
        }
    }
}