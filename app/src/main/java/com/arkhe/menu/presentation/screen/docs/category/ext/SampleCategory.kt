@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.category.ext

data class Category(
    val nama: String,
    val deskripsi: String,
    val productCount: Int,
)

val sampleCategory = listOf(
    Category(
        nama = "Series",
        deskripsi = "Pilihan tempat menginap terbaik untuk perjalanan Anda",
        productCount = 120
    ),
    Category(
        nama = "Workshop",
        deskripsi = "Berbagai opsi transportasi untuk memudahkan perjalanan Anda",
        productCount = 85
    ),
    Category(
        nama = "Other Side",
        deskripsi = "Destinasi wisata menarik yang wajib dikunjungi",
        productCount = 200
    ),
    Category(
        nama = "Chipkeun",
        deskripsi = "Tempat makan dan minum yang lezat dan populer",
        productCount = 150
    ),
    Category(
        nama = "Special",
        deskripsi = "Cendera mata khas daerah yang patut dibawa pulang",
        productCount = 60
    ),
    Category(
        nama = "Anniversary",
        deskripsi = "Tempat makan dan minum yang lezat dan populer",
        productCount = 150
    ),
    Category(
        nama = "Private",
        deskripsi = "Tempat makan dan minum yang lezat dan populer",
        productCount = 150
    ),
)