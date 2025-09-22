package com.arkhe.menu.presentation.screen.docs

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkhe.menu.utils.ScrollStateManager
import com.arkhe.menu.utils.rememberManagedScrollState

@SuppressLint("FrequentlyChangingValue")
@Composable
fun DocsScreen(
    scrollKey: String,
    scrollStateManager: ScrollStateManager,
    onNavigateToProfile: () -> Unit,
    onNavigateToOrganization: () -> Unit = {},
    onNavigateToCustomer: () -> Unit = {},
    onNavigateToCategories: () -> Unit = {},
    onNavigateToProducts: () -> Unit = {},
    onScrollAlphaChange: (Float) -> Unit = {}
) {
    val lazyListState = rememberManagedScrollState(
        key = scrollKey,
        scrollStateManager = scrollStateManager
    )

    LaunchedEffect(
        lazyListState.firstVisibleItemScrollOffset,
        lazyListState.firstVisibleItemIndex
    ) {
        val scrollAlpha = calculateScrollAlpha(lazyListState)
        onScrollAlphaChange(scrollAlpha)
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 20.dp)
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

private fun calculateScrollAlpha(lazyListState: LazyListState): Float {
    val firstVisibleIndex = lazyListState.firstVisibleItemIndex
    val firstVisibleOffset = lazyListState.firstVisibleItemScrollOffset

    return when {
        firstVisibleIndex == 0 && firstVisibleOffset < 100 -> {
            1f
        }

        firstVisibleIndex == 0 && firstVisibleOffset in 100..300 -> {
            1f - (firstVisibleOffset - 100) / 200f * 0.6f
        }

        else -> {
            0.4f
        }
    }
}