package com.arkhe.menu.domain.usecase

import com.arkhe.menu.domain.model.UserRole
import com.arkhe.menu.domain.repository.TripkeunRepository
import kotlinx.coroutines.flow.Flow

class GetUserRoleUseCase(
    private val repository: TripkeunRepository
) {
    suspend operator fun invoke(): Flow<UserRole> = repository.getUserRole()
}