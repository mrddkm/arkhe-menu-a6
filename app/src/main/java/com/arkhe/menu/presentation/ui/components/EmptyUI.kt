package com.arkhe.menu.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EmptyUI(
    message: String = "No Data Available ...",
    isButtonLoad: Boolean = true,
    onLoad: () -> Unit = { }
) {
    LoadingWaitingData(
        message = message,
        isButtonLoad = isButtonLoad,
        onLoad = onLoad
    )
}

@Preview(showBackground = true)
@Composable
fun EmptyUIPreview() {
    EmptyUI(
        message = "Select Product Groups",
        isButtonLoad = false
    )
}