package com.arkhe.menu.utils

import android.annotation.SuppressLint

sealed class DistanceResult {
    object Hidden : DistanceResult()
    object Incomplete : DistanceResult()
    data class Value(val number: String, val unit: String) : DistanceResult()
}

@SuppressLint("DefaultLocale")
fun String.toDistanceResult(): DistanceResult {
    val trimmed = this.trim()

    return when (trimmed) {
        "-" -> DistanceResult.Hidden
        "0" -> DistanceResult.Incomplete
        else -> {
            val meter = trimmed.toIntOrNull()
            when {
                meter == null || meter < 0 -> DistanceResult.Hidden
                meter == 0 -> DistanceResult.Incomplete
                meter >= 1000 -> {
                    val km = meter / 1000.0
                    DistanceResult.Value(
                        number = String.format("%.2f", km),
                        unit = "km"
                    )
                }
                else -> {
                    DistanceResult.Value(
                        number = meter.toString(),
                        unit = "m"
                    )
                }
            }
        }
    }
}