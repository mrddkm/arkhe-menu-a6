package com.arkhe.menu.presentation.viewmodel.receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.domain.model.Receipt
import com.arkhe.menu.domain.repository.TripkeunRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReceiptListViewModel(
    private val repository: TripkeunRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiptListUiState())
    val uiState: StateFlow<ReceiptListUiState> = _uiState.asStateFlow()

    init {
        loadReceipts()
    }

    private fun loadReceipts() {
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            repository.getReceipts().collect { receipts ->
                _uiState.value = _uiState.value.copy(
                    receipts = receipts,
                    isLoading = false
                )
            }
        }
    }

    fun refreshReceipts() {
        loadReceipts()
    }

    fun deleteReceipt(receiptId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteReceipt(receiptId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to delete receipt"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class ReceiptListUiState(
    val receipts: List<Receipt> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)