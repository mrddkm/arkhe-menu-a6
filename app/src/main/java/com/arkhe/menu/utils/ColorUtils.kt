package com.arkhe.menu.utils

import androidx.compose.ui.graphics.Color

fun getDevelopmentColor(status: String): Color {
    return when (status) {
        Constants.Category.STATISTICS_TOTAL -> Color(0xFF1C2526)
        Constants.Category.STATISTICS_READY -> Color(0xFF3A5F56)
        Constants.Category.STATISTICS_RESEARCH -> Color(0xFFE8B923)
        Constants.Category.STATISTICS_INITIATION -> Color(0xFFA8333E)
        else -> Color.Gray
    }
}