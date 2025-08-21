@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import com.arkhe.menu.data.remote.dto.TripkeunDto
import com.arkhe.menu.data.remote.dto.ReceiptDto
import com.arkhe.menu.presentation.utils.Constants

class TripkeunApiService(
    private val httpClient: HttpClient
) {

    suspend fun getTripData(): List<TripkeunDto> {
        return httpClient.get("${Constants.API_BASE_URL}/trips").body()
    }

    suspend fun getTripById(id: Long): TripkeunDto {
        return httpClient.get("${Constants.API_BASE_URL}/trips/$id").body()
    }

    suspend fun createTrip(trip: TripkeunDto): TripkeunDto {
        return httpClient.post("${Constants.API_BASE_URL}/trips") {
            setBody(trip)
        }.body()
    }

    suspend fun getReceipts(): List<ReceiptDto> {
        return httpClient.get("${Constants.API_BASE_URL}/receipts").body()
    }

    suspend fun createReceipt(receipt: ReceiptDto): ReceiptDto {
        return httpClient.post("${Constants.API_BASE_URL}/receipts") {
            setBody(receipt)
        }.body()
    }

    suspend fun getReceiptById(id: Long): ReceiptDto {
        return httpClient.get("${Constants.API_BASE_URL}/receipts/$id").body()
    }
}