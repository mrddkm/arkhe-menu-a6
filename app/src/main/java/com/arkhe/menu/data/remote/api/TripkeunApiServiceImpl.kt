package com.arkhe.menu.data.remote.api

import com.arkhe.menu.data.remote.dto.ProfileRequestDto
import com.arkhe.menu.data.remote.dto.ProfileResponseDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

interface TripkeunApiService {
    suspend fun getProfiles(sessionToken: String): ProfileResponseDto
}

class TripkeunApiServiceImpl(
    private val httpClient: HttpClient
) : TripkeunApiService {

    companion object {
        private const val BASE_URL = "https://script.google.com/macros/s/AKfycbxzU5sK4C50geQL1-PWtFm8NeQTErOcY0QGw8XvMfFUOTPKgAYYBZO8p5UuAMVeJ1PCQw/exec"
    }

    override suspend fun getProfiles(sessionToken: String): ProfileResponseDto {
        val response: HttpResponse = httpClient.post {
            url(BASE_URL)
            parameter("action", "profiles")
            contentType(ContentType.Application.Json)
            setBody(ProfileRequestDto(sessionToken = sessionToken))
        }

        // Check if response is successful
        if (response.status.isSuccess()) {
            return response.body<ProfileResponseDto>()
        } else {
            throw Exception("HTTP ${response.status.value}: ${response.status.description}")
        }
    }
}