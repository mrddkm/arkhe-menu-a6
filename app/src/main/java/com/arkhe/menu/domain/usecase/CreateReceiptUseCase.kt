package com.arkhe.menu.domain.usecase

import com.arkhe.menu.domain.model.Receipt
import com.arkhe.menu.domain.repository.TripkeunRepository

class CreateReceiptUseCase(
    private val repository: TripkeunRepository
) {
    suspend operator fun invoke(receipt: Receipt): Result<Long> {
        return try {
            val receiptId = repository.createReceipt(receipt)
            Result.success(receiptId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}