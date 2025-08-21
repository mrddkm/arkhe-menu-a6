package com.arkhe.menu.presentation.utils

import android.util.Patterns

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhone(phone: String): Boolean {
        return phone.isNotBlank() && phone.matches(Regex("^[+]?[0-9]{10,13}$"))
    }

    fun isValidCustomerName(name: String): Boolean {
        return name.isNotBlank() && name.length >= Constants.MIN_CUSTOMER_NAME_LENGTH
    }

    fun isValidItemName(name: String): Boolean {
        return name.isNotBlank() && name.trim().length >= 2
    }

    fun isValidQuantity(quantity: String): Boolean {
        val qty = quantity.toIntOrNull()
        return qty != null && qty > 0 && qty <= 1000
    }

    fun isValidPrice(price: String): Boolean {
        val amount = price.toDoubleOrNull()
        return amount != null && amount >= 0 && amount <= 999999999.99
    }

    fun isValidReceiptData(
        customerName: String,
        items: List<Any>,
        email: String? = null,
        phone: String? = null
    ): ValidationResult {
        val errors = mutableListOf<String>()

        if (!isValidCustomerName(customerName)) {
            errors.add("Customer name must be at least ${Constants.MIN_CUSTOMER_NAME_LENGTH} characters")
        }

        if (items.isEmpty()) {
            errors.add("At least one item is required")
        }

        if (items.size > Constants.MAX_RECEIPT_ITEMS) {
            errors.add("Maximum ${Constants.MAX_RECEIPT_ITEMS} items allowed")
        }

        email?.let {
            if (it.isNotBlank() && !isValidEmail(it)) {
                errors.add("Invalid email format")
            }
        }

        phone?.let {
            if (it.isNotBlank() && !isValidPhone(it)) {
                errors.add("Invalid phone number format")
            }
        }

        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }
}

data class ValidationResult(
    val isValid: Boolean,
    val errors: List<String>
)