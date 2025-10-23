package com.arkhe.menu.data.repository

import com.arkhe.menu.data.remote.dto.VerificationResponseDto

interface VerificationService {
    suspend fun verification(
        userId: String,
        phone: String,
        mail: String
    ): VerificationResponseDto
}
