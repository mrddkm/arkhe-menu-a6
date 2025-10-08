package com.arkhe.menu.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.collections.find

@Parcelize
data class Language(
    val code: String,
    val name: String,
    val nativeName: String
) : Parcelable

object Languages {
    val ENGLISH = Language("en", "United States", "English")
    val INDONESIAN = Language("in", "Indonesian", "Bahasa Indonesia")

    val availableLanguages = listOf(ENGLISH, INDONESIAN)

    fun getLanguageByCode(code: String): Language {
        return availableLanguages.find { it.code == code } ?: ENGLISH
    }
}