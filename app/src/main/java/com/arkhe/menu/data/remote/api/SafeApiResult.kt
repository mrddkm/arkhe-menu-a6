package com.arkhe.menu.data.remote.api

/**
 * Result wrapper for API responses
 * Sealed Class to handle various states from API Call
 */
sealed class SafeApiResult<out T> {
    data class Success<out T>(val data: T) : SafeApiResult<T>()
    data class Failure(val exception: Throwable) : SafeApiResult<Nothing>()
    data object Loading : SafeApiResult<Nothing>()
}