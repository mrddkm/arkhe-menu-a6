@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.utils

import com.arkhe.menu.domain.model.PasswordData
import com.arkhe.menu.domain.model.PinData
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.domain.model.ProductActionInfo
import com.arkhe.menu.domain.model.ProductInformationLanguage
import com.arkhe.menu.domain.model.User
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_READY

val sampleProduct = Product(
    id = "rtg6wm5iijqC5WIl",
    productCategoryId = "SRS",
    categoryName = "Series",
    categoryType = "Regular",
    productCode = "MN04",
    productFullName = "Mountain Series #04",
    productTagLine = "Goes to Papandayan",
    productDestination = "Gn. Papandayan",
    logo = "",
    status = STATISTICS_READY,
    hikeDistance = "8390",
    hikeDuration = "00:00",
    hikeElevationGain = "467",
    hikeAltitude = "-",
    hikeLevelId = "4",
    hikeLevelName = "Moderate",
    hikeLevelInformation = ProductInformationLanguage(
        indonesian = "Tingkat Menengah (Moderate): Level ini menantang pendaki yang sudah memiliki pengalaman dan kondisi fisik yang lebih baik. Jalur pendakiannya bisa lebih bervariasi, seperti melewati medan yang berbatu, akar pohon, atau tanjakan yang lebih curam. Durasi pendakiannya bisa lebih dari satu hari, seringkali memerlukan bermalam di alam terbuka. Pendaki mungkin perlu membawa perlengkapan yang lebih lengkap, termasuk tenda, kompor, dan perlengkapan navigasi. Terkadang, ada juga jalur yang memerlukan sedikit penggunaan tali atau alat bantu lain, tapi tidak terlalu rumit.",
        english = "Moderate Level: This level challenges hikers who already have some experience and better physical condition. The climbing path can be more varied, such as traversing rocky terrain, tree roots, or steeper inclines. The duration of the climb can be more than one day, often requiring an overnight stay outdoors. Hikers may need to bring more complete gear, including a tent, stove, and navigation equipment. Sometimes, there are also paths that require a little use of ropes or other tools, but are not overly complex."
    ),
    information = ProductInformationLanguage(
        indonesian = "Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an",
        english = "Lorem ipsum dolor sit amet consectetur adipiscing elit. Sit amet consectetur adipiscing elit quisque faucibus ex. Adipiscing elit quisque faucibus ex sapien vitae pellentesque."
    ),
    actionInfo = ProductActionInfo(
        action = "product",
        information = ProductInformationLanguage(
            indonesian = "Lorem Ipsum hanyalah contoh teks dalam industri percetakan dan penataan huruf.",
            english = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
        )
    ),
    localImagePath = null
)

val sampleUser = User(
    id = "1",
    userId = "230504",
    mail = "didik.muttaqien@gmail.com",
    name = "DIDIK MUTTAQIEN",
    phone = "6285659988939",
    initial = "DM",
    nickName = "mrddkm",
    birthday = "29-03-1985",
    gender = "Male",
    photo = "",
    positionId = "cbo",
    positionInitial = "CBO",
    positionName = "Director of BROD",
    divisionId = "brod",
    divisionInitial = "BROD",
    divisionName = "Business, Research, Operational & Development",
    roles = "moderator",
    status = "active",
    sessionActivation = "uPwb5rCTolKz3HsC",
    sessionActivationAt = "2025-10-09T18=59=31",
    sessionToken = "ct85o4Kv1s5uiLsr",
    sessionExpiredAt = "2025-11-08T20=10=20",
    createdAt = "",
    updatedAt = "",
    isBiometricActive = true,
    lastActiveAt = "2025-10-09T20=10=20",
)

val samplePasswordData = PasswordData()

val samplePinData = PinData()