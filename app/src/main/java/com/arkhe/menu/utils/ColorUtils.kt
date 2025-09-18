package com.arkhe.menu.utils

import androidx.compose.ui.graphics.Color
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_INITIATION
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_READY
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_RESEARCH
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_TOTAL

fun getDevelopmentColor(status: String): Color {
    return when (status) {
        STATISTICS_TOTAL -> Color(0xFF1C2526)
        STATISTICS_READY -> Color(0xFF3A5F56)
        STATISTICS_RESEARCH -> Color(0xFFE8B923)
        STATISTICS_INITIATION -> Color(0xFFA8333E)
        else -> Color.Gray
    }
}