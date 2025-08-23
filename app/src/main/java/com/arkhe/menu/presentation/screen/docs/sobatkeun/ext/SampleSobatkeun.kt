@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.sobatkeun.ext

data class Sobatkeun(
    val id: String,
    val nama: String,
    val tripCount: Int,
    val deskripsi: String,
    val email: String,
    val instagram: String,
    val whatsapp: String,
)

val sampleSobatkeun = listOf(
    Sobatkeun(
        id = "1",
        nama = "Rina Kusuma",
        tripCount = 25,
        deskripsi = "Travel Enthusiast & Blogger",
        email = "rina_travels@gmail.com",
        instagram = "rina_travels",
        whatsapp = "+62 812-3456-7890"
    ),
    Sobatkeun(
        id = "2",
        nama = "Dedi Saputra",
        tripCount = 40,
        deskripsi = "Adventure Seeker & Photographer",
        email = "",
        instagram = "dedi_adventures",
        whatsapp = "+62 813-4567-8901"
    ),
    Sobatkeun(
        id = "3",
        nama = "Maya Lestari",
        tripCount = 15,
        deskripsi = "Cultural Explorer & Foodie",
        email = "",
        instagram = "maya_culture",
        whatsapp = "+62 814-5678-9012"
    )
)