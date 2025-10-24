package com.arkhe.menu.data.mapper

import com.arkhe.menu.data.remote.dto.SignInResponseDto
import com.arkhe.menu.domain.model.auth.SignInResponse

fun SignInResponseDto.toDomain(): SignInResponse {
    return SignInResponse(
        status = this.status,
        message = this.message,
        sessionToken = this.data?.sessionToken
    )
}
