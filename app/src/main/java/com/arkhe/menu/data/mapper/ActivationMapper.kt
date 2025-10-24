package com.arkhe.menu.data.mapper

import com.arkhe.menu.data.remote.dto.ActivationResponseDto
import com.arkhe.menu.domain.model.auth.ActivationResponse

/*DTO to Domain (direct conversion)*/
fun ActivationResponseDto.toDomain(): ActivationResponse {
    val session = this.data?.sessionActivation

    return ActivationResponse(
        status = this.status,
        message = this.message,
        sessionActivation = session
    )
}