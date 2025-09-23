@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.utils

import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_RESEARCH

val sampleProduct = Product(
    id = "rtg6wm5iijqC5WIl",
    productCategoryId = "SRS",
    categoryName = "Series",
    categoryType = "Regular",
    productCode = "MN04",
    productFullName = "Mountain Series #04",
    productDestination = "Gn. Papandayan",
    logo = "",
    status = STATISTICS_RESEARCH,
    information = com.arkhe.menu.domain.model.ProductInformationLanguage(
        indonesian = "Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an",
        english = "Lorem ipsum dolor sit amet consectetur adipiscing elit. Sit amet consectetur adipiscing elit quisque faucibus ex. Adipiscing elit quisque faucibus ex sapien vitae pellentesque."
    ),
    actionInfo = com.arkhe.menu.domain.model.ProductActionInfo(
        action = "product",
        information = com.arkhe.menu.domain.model.ProductInformationLanguage(
            indonesian = "Lorem Ipsum hanyalah contoh teks dalam industri percetakan dan penataan huruf. Lorem Ipsum telah menjadi contoh teks standar industri sejak tahun 1500-an.",
            english = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s."
        )
    ),
    localImagePath = null
)