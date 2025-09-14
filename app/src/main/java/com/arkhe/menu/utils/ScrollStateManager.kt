@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * ScrollStateManager untuk menyimpan dan memulihkan scroll state
 */
class ScrollStateManager {
    private val scrollStates = mutableMapOf<String, ScrollStateInfo>()

    data class ScrollStateInfo(
        val firstVisibleItemIndex: Int = 0,
        val firstVisibleItemScrollOffset: Int = 0
    )

    fun saveScrollState(key: String, lazyListState: LazyListState) {
        val info = ScrollStateInfo(
            firstVisibleItemIndex = lazyListState.firstVisibleItemIndex,
            firstVisibleItemScrollOffset = lazyListState.firstVisibleItemScrollOffset
        )
        scrollStates[key] = info
    }

    fun getScrollState(key: String): ScrollStateInfo {
        val state = scrollStates[key] ?: ScrollStateInfo()
        return state
    }

    fun restoreScrollState(key: String): LazyListState {
        val savedState = getScrollState(key)
        return LazyListState(
            firstVisibleItemIndex = savedState.firstVisibleItemIndex,
            firstVisibleItemScrollOffset = savedState.firstVisibleItemScrollOffset
        )
    }

    @Suppress("UNUSED")
    fun clearScrollState(key: String) {
        scrollStates.remove(key)
    }

    fun clearAllScrollStates() {
        scrollStates.clear()
    }

    @Suppress("UNUSED")
    fun getAllStates(): Map<String, ScrollStateInfo> = scrollStates.toMap()
}

/**
 * Composable helper untuk mengelola scroll state dengan auto-save
 */
@Composable
fun rememberManagedScrollState(
    key: String,
    scrollStateManager: ScrollStateManager
): LazyListState {
    val lazyListState = remember(key) {
        scrollStateManager.restoreScrollState(key)
    }

    LaunchedEffect(key, lazyListState) {
        snapshotFlow {
            lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset
        }
            .distinctUntilChanged()
            .collect { (index, offset) ->
                if (index > 0 || offset > 0) {
                    scrollStateManager.saveScrollState(key, lazyListState)
                }
            }
    }

    DisposableEffect(key) {
        onDispose {
            scrollStateManager.saveScrollState(key, lazyListState)
        }
    }

    return lazyListState
}