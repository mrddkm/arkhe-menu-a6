package com.arkhe.menu.data.mapper

import com.arkhe.menu.data.remote.dto.ActivationResponseDto
import com.arkhe.menu.domain.model.auth.ActivationResponse

/*DTO to Domain (direct conversion)*/
fun ActivationResponseDto.toDomain(): ActivationResponse {
    return ActivationResponse(
        status = this.status,
        message = this.message,
        userId = this.data?.userId,
        name = this.data?.name,
        sessionActivation = this.data?.sessionActivation
    )
}