package com.arkhe.menu.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.local.preferences.LanguageLocalDataSource
import com.arkhe.menu.data.local.preferences.LanguageLocalizedStrings
import com.arkhe.menu.domain.model.LanguageModels
import com.arkhe.menu.domain.model.Languages
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LanguageViewModel(
    application: Application,
    private val languageLocalDataSource: LanguageLocalDataSource
) : AndroidViewModel(application) {

    private val _languageState = MutableStateFlow(LanguageState())
    val languageState: StateFlow<LanguageState> = _languageState.asStateFlow()

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    init {
        observeLanguageChanges()
    }

    private fun observeLanguageChanges() {
        viewModelScope.launch {
            languageLocalDataSource.selectedLanguageCode.collect { languageCode ->
                val language = Languages.getLanguageByCode(languageCode)
                val localizedStrings = loadLocalizedStrings(languageCode)

                _languageState.value = _languageState.value.copy(
                    currentLanguage = language,
                    localizedStrings = localizedStrings
                )
            }
        }
    }

    fun getLocalizedString(key: String): String {
        return _languageState.value.localizedStrings[key] ?: ""
    }

    fun showLanguageSelector() {
        _showBottomSheet.value = true
    }

    fun hideLanguageSelector() {
        _showBottomSheet.value = false
    }

    fun selectLanguage(languageModels: LanguageModels) {
        viewModelScope.launch {
            val currentLanguage = _languageState.value.currentLanguage

            if (currentLanguage.code != languageModels.code) {
                _languageState.value = _languageState.value.copy(isChangingLanguage = true)
                _showBottomSheet.value = false

                languageLocalDataSource.setLanguage(languageModels.code)

                delay(800)

                val newLocalizedStrings = loadLocalizedStrings(languageModels.code)

                _languageState.value = _languageState.value.copy(
                    currentLanguage = languageModels,
                    localizedStrings = newLocalizedStrings,
                    isChangingLanguage = false
                )
            } else {
                _showBottomSheet.value = false
            }
        }
    }

    private fun loadLocalizedStrings(languageCode: String): Map<String, String> {
        return LanguageLocalizedStrings.getLocalizedStringsMap(getApplication(), languageCode)
    }
}

data class LanguageState(
    val currentLanguage: LanguageModels = Languages.ENGLISH,
    val isChangingLanguage: Boolean = false,
    val localizedStrings: Map<String, String> = emptyMap()
)