package com.arkhe.menu.presentation.viewmodel.receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.domain.model.PaymentMethod
import com.arkhe.menu.domain.model.PaymentStatus
import com.arkhe.menu.domain.model.Receipt
import com.arkhe.menu.domain.model.ReceiptItem
import com.arkhe.menu.domain.usecase.CreateReceiptUseCase
import com.arkhe.menu.presentation.utils.generateReceiptNumber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class CreateReceiptViewModel(
    private val createReceiptUseCase: CreateReceiptUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReceiptUiState())
    val uiState: StateFlow<CreateReceiptUiState> = _uiState.asStateFlow()

    fun updateCustomerName(name: String) {
        _uiState.value = _uiState.value.copy(customerName = name)
    }

    fun updateCustomerEmail(email: String) {
        _uiState.value = _uiState.value.copy(customerEmail = email)
    }

    fun updateCustomerPhone(phone: String) {
        _uiState.value = _uiState.value.copy(customerPhone = phone)
    }

    fun updatePaymentMethod(method: PaymentMethod) {
        _uiState.value = _uiState.value.copy(paymentMethod = method)
    }

    fun updateNotes(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }

    fun addItem(item: ReceiptItem) {
        val currentItems = _uiState.value.items.toMutableList()
        currentItems.add(item)
        updateCalculations(currentItems)
    }

    fun removeItem(index: Int) {
        val currentItems = _uiState.value.items.toMutableList()
        if (index < currentItems.size) {
            currentItems.removeAt(index)
            updateCalculations(currentItems)
        }
    }

    fun updateItem(index: Int, item: ReceiptItem) {
        val currentItems = _uiState.value.items.toMutableList()
        if (index < currentItems.size) {
            currentItems[index] = item
            updateCalculations(currentItems)
        }
    }

    private fun updateCalculations(items: List<ReceiptItem>) {
        val subtotal = items.sumOf { it.totalPrice }
        val tax = subtotal * 0.1 // 10% tax
        val total = subtotal + tax - _uiState.value.discount

        _uiState.value = _uiState.value.copy(
            items = items,
            subtotal = subtotal,
            tax = tax,
            total = total
        )
    }

    fun createReceipt() {
        if (!isValidForm()) return

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val receipt = Receipt(
                receiptNumber = generateReceiptNumber(),
                customerName = _uiState.value.customerName,
                customerEmail = _uiState.value.customerEmail.takeIf { it.isNotBlank() },
                customerPhone = _uiState.value.customerPhone.takeIf { it.isNotBlank() },
                items = _uiState.value.items,
                subtotal = _uiState.value.subtotal,
                tax = _uiState.value.tax,
                discount = _uiState.value.discount,
                total = _uiState.value.total,
                paymentMethod = _uiState.value.paymentMethod,
                paymentStatus = PaymentStatus.PENDING,
                notes = _uiState.value.notes.takeIf { it.isNotBlank() },
                createdBy = "current_user", // TODO: Get from auth
                createdAt = Date(),
                updatedAt = Date()
            )

            val result = createReceiptUseCase(receipt)

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Unknown error occurred"
                )
            }
        }
    }

    private fun isValidForm(): Boolean {
        val state = _uiState.value
        return state.customerName.isNotBlank() && state.items.isNotEmpty()
    }

    fun clearForm() {
        _uiState.value = CreateReceiptUiState()
    }
}

data class CreateReceiptUiState(
    val customerName: String = "",
    val customerEmail: String = "",
    val customerPhone: String = "",
    val items: List<ReceiptItem> = emptyList(),
    val subtotal: Double = 0.0,
    val tax: Double = 0.0,
    val discount: Double = 0.0,
    val total: Double = 0.0,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val notes: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)