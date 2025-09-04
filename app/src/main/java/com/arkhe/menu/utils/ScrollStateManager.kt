package com.arkhe.menu.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun rememberScrollStateWithPosition(
    initialPosition: Int = 0,
    onScrollPositionChanged: (Int) -> Unit = {}
): LazyListState {
    val listState = androidx.compose.foundation.lazy.rememberLazyListState(
        initialFirstVisibleItemIndex = initialPosition
    )

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { position ->
                onScrollPositionChanged(position)
            }
    }

    return listState
}