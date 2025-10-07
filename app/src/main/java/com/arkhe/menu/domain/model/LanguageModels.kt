package com.arkhe.menu.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.collections.find

@Parcelize
data class LanguageModels(
    val code: String,
    val name: String,
    val nativeName: String
) : Parcelable

object Languages {
    val ENGLISH = LanguageModels("en", "United States", "English")
    val INDONESIAN = LanguageModels("in", "Indonesian", "Bahasa Indonesia")

    val availableLanguages = listOf(ENGLISH, INDONESIAN)

    fun getLanguageByCode(code: String): LanguageModels {
        return availableLanguages.find { it.code == code } ?: ENGLISH
    }
}
