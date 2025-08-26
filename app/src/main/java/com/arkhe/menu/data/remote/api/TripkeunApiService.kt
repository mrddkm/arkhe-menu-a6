package com.arkhe.menu.data.remote.api

import com.arkhe.menu.data.remote.dto.ProfileRequestDto
import com.arkhe.menu.data.remote.dto.ProfileResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

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
        return httpClient.post {
            url(BASE_URL)
            parameter("action", "profiles")
            contentType(ContentType.Application.Json)
            setBody(ProfileRequestDto(sessionToken = sessionToken))
        }.body()
    }
}