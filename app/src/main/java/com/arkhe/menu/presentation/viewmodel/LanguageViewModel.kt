package com.arkhe.menu.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.local.preferences.LanguageLocalizedStrings
import com.arkhe.menu.domain.model.Language
import com.arkhe.menu.domain.model.LanguageState
import com.arkhe.menu.domain.model.Languages
import com.arkhe.menu.domain.repository.ILanguageRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LanguageViewModel(
    context: Context,
    private val languageRepository: ILanguageRepository
) : ViewModel() {

    private val appContext: Context = context.applicationContext

    private val _languageState = MutableStateFlow(
        LanguageState(
            currentLanguage = Languages.ENGLISH,
            localizedStrings = loadLocalized(Languages.ENGLISH.code)
        )
    )

    val languageState: StateFlow<LanguageState> = _languageState.asStateFlow()

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    init {
        observeLanguageChanges()
    }

    private fun observeLanguageChanges() {
        viewModelScope.launch {
            languageRepository.selectedLanguageCode.collect { languageCode ->
                val language = Languages.getLanguageByCode(languageCode)
                val localizedStrings = loadLocalized(languageCode)

                _languageState.value = _languageState.value.copy(
                    currentLanguage = language,
                    localizedStrings = localizedStrings
                )
            }
        }
    }

    private fun loadLocalized(languageCode: String): Map<String, String> {
        return LanguageLocalizedStrings.getLocalizedStringsMap(appContext, languageCode)
    }

    fun getLocalized(key: String): String {
        return _languageState.value.localizedStrings[key] ?: ""
    }

    fun showLanguageSelector() {
        _showBottomSheet.value = true
    }

    fun hideLanguageSelector() {
        _showBottomSheet.value = false
    }

    fun selectLanguage(language: Language) {
        viewModelScope.launch {
            val currentLanguage = _languageState.value.currentLanguage

            if (currentLanguage.code != language.code) {
                _languageState.value = _languageState.value.copy(isChangingLanguage = true)
                _showBottomSheet.value = false

                languageRepository.setLanguage(language.code)

                delay(800)

                val newLocalizedStrings = loadLocalized(language.code)

                _languageState.value = _languageState.value.copy(
                    currentLanguage = language,
                    localizedStrings = newLocalizedStrings,
                    isChangingLanguage = false
                )
            } else {
                _showBottomSheet.value = false
            }
        }
    }
}