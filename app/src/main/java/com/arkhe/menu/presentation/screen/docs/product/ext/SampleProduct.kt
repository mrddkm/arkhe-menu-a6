@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.product.ext

data class Product(
    val id: String,
    val productCategoryId: String,
    val productCode: String,
    val productFullName: String,
    val productDestination: String,
    val status: String,
    val additionalInfo: String,
)

val sampleProduct = listOf(
    Product(
        id = "qGsjmSivdEGMB6zL",
        productCategoryId = "Series",
        productCode = "WF01",
        productFullName = "Waterfalls Series #01",
        productDestination = "Curug Bugbrug",
        status = "Product",
        additionalInfo = "A luxurious trip with modern amenities."
    ),
    Product(
        id = "xm1wV9ya5L6SO0ZQ",
        productCategoryId = "Series",
        productCode = "WF02",
        productFullName = "Waterfalls Series #02",
        productDestination = "Curug Layung",
        status = "Product",
        additionalInfo = "A comfortable trip with essential facilities."
    ),
    Product(
        id = "G2V1V6b9tp1e21Aj",
        productCategoryId = "Series",
        productCode = "FT01",
        productFullName = "Forest Series #01",
        productDestination = "Batu Belang",
        status = "Product",
        additionalInfo = "Experience nature with basic comforts."
    ),
    Product(
        id = "cVPdlTda1dLdBlIL",
        productCategoryId = "Series",
        productCode = "FT02",
        productFullName = "Forest Series #02",
        productDestination = "Pasir Ipis",
        status = "Product",
        additionalInfo = "Enjoy the wilderness with standard amenities."
    ),
    Product(
        id = "wF9aImQjHcg2qqSB",
        productCategoryId = "Workshop",
        productCode = "WP1",
        productFullName = "Workshop Series #01",
        productDestination = "Photography",
        status = "Product",
        additionalInfo = "A half-day workshop on photography skills."
    ),
    Product(
        id = "dCx8uFMSddpMHUbm",
        productCategoryId = "Other Side",
        productCode = "TOS01",
        productFullName = "T.O.S.T. Camping Ceria",
        productDestination = "Pagerwangi Dome",
        status = "Product",
        additionalInfo = "Camping experience with fun activities."
    ),
    Product(
        id = "RJyMuZb2DRM8stEK",
        productCategoryId = "Other Side",
        productCode = "TOS10",
        productFullName = "T.O.S.T. Sejarasa",
        productDestination = "Sumedang-Majalengka-Cirebon",
        status = "Product",
        additionalInfo = "Culinary tour with historical insights."
    ),
    Product(
        id = "kiSCIke4dVDT43Xw",
        productCategoryId = "Chipkeun",
        productCode = "CIP01",
        productFullName = "Chipkeun Cemal Cemil Bareng Kamil",
        productDestination = "Kuliner",
        status = "Product",
        additionalInfo = "A culinary experience with local snacks."
    ),
    Product(
        id = "IHv2c0XwHvHMmmxH",
        productCategoryId = "Chipkeun",
        productCode = "CIP04",
        productFullName = "Chipkeun #04 Gn. Pangradinan",
        productDestination = "Gn. Pangradinan",
        status = "Product",
        additionalInfo = "A trip experience with local tour."
    ),
    Product(
        id = "8SCOo3KKRWBwIf71",
        productCategoryId = "Special",
        productCode = "FKS",
        productFullName = "Flashback Stories Thee Onderneming Malabar",
        productDestination = "Malabar",
        status = "Product",
        additionalInfo = "A historical tour with storytelling."
    ),
    Product(
        id = "P8w8jxyzoPVZMTS7",
        productCategoryId = "Private",
        productCode = "PRV",
        productFullName = "Private Trip",
        productDestination = "Custom Trip",
        status = "Product",
        additionalInfo = "A trip experience with custom tour."
    ),
)