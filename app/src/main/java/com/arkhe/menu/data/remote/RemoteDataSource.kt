package com.arkhe.menu.data.remote

import com.arkhe.menu.data.remote.api.TripkeunApiService
import com.arkhe.menu.data.remote.dto.ProfileResponseDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataSource(
    private val apiService: TripkeunApiService
) {
    suspend fun getProfiles(sessionToken: String): ProfileResponseDto {
        return withContext(Dispatchers.IO) {
            apiService.getProfiles(sessionToken)
        }
    }
}