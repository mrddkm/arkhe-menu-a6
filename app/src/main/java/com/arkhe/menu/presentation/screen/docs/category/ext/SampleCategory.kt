@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.category.ext

import androidx.compose.ui.graphics.Color

data class Categories(
    val nama: String,
    val deskripsi: String,
    val productCount: Int,
)

enum class CategoryColor(val backgroundColor: Color, val iconColor: Color) {
    SERIES(Color(0xFFE8F5E8), Color(0xFF2E7D32)),        // Hijau
    WORKSHOP(Color(0xFFE3F2FD), Color(0xFF1976D2)),      // Biru
    OTHER_SIDE(Color(0xFFF3E5F5), Color(0xFF7B1FA2)),    // Ungu
    CHIPKEUN(Color(0xFFFFF3E0), Color(0xFFEF6C00)),      // Orange
    SPECIAL(Color(0xFFFFEBEE), Color(0xFFD32F2F)),       // Merah
    ANNIVERSARY(Color(0xFFF1F8E9), Color(0xFF388E3C)),   // Hijau Muda
    PRIVATE(Color(0xFFE0F2F1), Color(0xFF00695C)),       // Teal
    DEFAULT(Color(0xFFE0E0E0), Color(0xFF616161))        // Abu-abu default
}

fun getCategoryColor(categoryName: String): CategoryColor {
    return when (categoryName.uppercase()) {
        "SERIES" -> CategoryColor.SERIES
        "WORKSHOP" -> CategoryColor.WORKSHOP
        "OTHER SIDE" -> CategoryColor.OTHER_SIDE
        "CHIPKEUN" -> CategoryColor.CHIPKEUN
        "SPECIAL" -> CategoryColor.SPECIAL
        "ANNIVERSARY" -> CategoryColor.ANNIVERSARY
        "PRIVATE" -> CategoryColor.PRIVATE
        else -> CategoryColor.DEFAULT
    }
}

val sampleCategories = listOf(
    Categories(
        nama = "Series",
        deskripsi = "Pilihan tempat menginap terbaik untuk perjalanan Anda",
        productCount = 36
    ),
    Categories(
        nama = "Workshop",
        deskripsi = "Berbagai opsi transportasi untuk memudahkan perjalanan Anda",
        productCount = 2
    ),
    Categories(
        nama = "Other Side",
        deskripsi = "Destinasi wisata menarik yang wajib dikunjungi",
        productCount = 9
    ),
    Categories(
        nama = "Chipkeun",
        deskripsi = "Tempat makan dan minum yang lezat dan populer",
        productCount = 12
    ),
    Categories(
        nama = "Special",
        deskripsi = "Cendera mata khas daerah yang patut dibawa pulang",
        productCount = 3
    ),
    Categories(
        nama = "Anniversary",
        deskripsi = "Tempat makan dan minum yang lezat dan populer",
        productCount = 1
    ),
    Categories(
        nama = "Private",
        deskripsi = "Tempat makan dan minum yang lezat dan populer",
        productCount = 1
    ),
)