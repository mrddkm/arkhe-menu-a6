package com.arkhe.menu.data.mapper

import com.arkhe.menu.data.remote.dto.VerificationResponseDto
import com.arkhe.menu.domain.model.auth.Verification

/*DTO to Domain (direct conversion)*/
fun VerificationResponseDto.toDomain(): Verification {
    val dataDto = this.data
        ?: throw IllegalStateException("Verification data cannot be null when converting to domain model")

    return Verification(
        userId = dataDto.userId,
        name = dataDto.name
    )
}