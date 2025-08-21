package com.arkhe.menu.domain.usecase

import com.arkhe.menu.domain.model.TripkeunData
import com.arkhe.menu.domain.repository.TripkeunRepository
import kotlinx.coroutines.flow.Flow

class GetTripDataUseCase(
    private val repository: TripkeunRepository
) {
    suspend operator fun invoke(): Flow<List<TripkeunData>> = repository.getTripData()
}