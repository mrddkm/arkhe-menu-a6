@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.viewmodel.docs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.domain.usecase.GetTripDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DocsViewModel(
    private val getTripDataUseCase: GetTripDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DocsUiState())
    val uiState: StateFlow<DocsUiState> = _uiState.asStateFlow()

    init {
        loadDocsData()
    }

    private fun loadDocsData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Load docs related data
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    profileTripkeun = "Profile perusahaan Tripkeun lengkap dengan visi misi",
                    teamInfo = "Informasi lengkap tim Tripkeun",
                    topSobatkeun = "Daftar 10 member terbaik bulan ini",
                    productCategories = "Kategori produk tersedia",
                    products = "Daftar semua produk Tripkeun"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class DocsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val profileTripkeun: String = "",
    val teamInfo: String = "",
    val topSobatkeun: String = "",
    val productCategories: String = "",
    val products: String = ""
)