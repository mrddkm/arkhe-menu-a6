@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.data.remote

import com.arkhe.menu.data.remote.dto.AuthResponse
import com.arkhe.menu.data.remote.dto.LoginRequest
import com.arkhe.menu.data.remote.dto.ReceiptDto
import com.arkhe.menu.data.remote.dto.TripDataDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class TripkeunApiService(private val client: HttpClient) {

    companion object {
        private const val BASE_URL = "https://api.tripkeun.com/v1"
    }

    // Trip endpoints
    suspend fun getTrips(): Result<List<TripDataDto>> {
        return try {
            val response = client.get("$BASE_URL/trips")
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTripById(id: Long): Result<TripDataDto> {
        return try {
            val response = client.get("$BASE_URL/trips/$id")
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTrip(trip: TripDataDto): Result<TripDataDto> {
        return try {
            val response = client.post("$BASE_URL/trips") {
                contentType(ContentType.Application.Json)
                setBody(trip)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Receipt endpoints
    suspend fun getReceipts(): Result<List<ReceiptDto>> {
        return try {
            val response = client.get("$BASE_URL/receipts")
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createReceipt(receipt: ReceiptDto): Result<ReceiptDto> {
        return try {
            val response = client.post("$BASE_URL/receipts") {
                contentType(ContentType.Application.Json)
                setBody(receipt)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Authentication
    suspend fun login(username: String, password: String): Result<AuthResponse> {
        return try {
            val response = client.post("$BASE_URL/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}