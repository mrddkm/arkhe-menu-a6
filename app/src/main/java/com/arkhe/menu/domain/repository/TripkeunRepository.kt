@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.domain.repository

import com.arkhe.menu.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

interface TripkeunRepository {
    suspend fun getUserRole(): Flow<UserRole>
    suspend fun setUserRole(role: UserRole)
}