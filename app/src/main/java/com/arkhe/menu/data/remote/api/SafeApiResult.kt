package com.arkhe.menu.data.remote.api

/**
 * Result wrapper for API responses
 * Sealed Class to handle various states from API Call
 */
sealed class SafeApiResult<out T> {
    data class Success<out T>(val data: T) : SafeApiResult<T>()
    data class Error(val exception: Throwable) : SafeApiResult<Nothing>()
    data object Loading : SafeApiResult<Nothing>()
}

/**
 * Extension function to map SafeApiResult to other forms
 */
inline fun <T, R> SafeApiResult<T>.map(transform: (T) -> R): SafeApiResult<R> {
    return when (this) {
        is SafeApiResult.Success -> SafeApiResult.Success(transform(data))
        is SafeApiResult.Error -> SafeApiResult.Error(exception)
        is SafeApiResult.Loading -> SafeApiResult.Loading
    }
}

/**
 * Extension function to get data or null
 */
fun <T> SafeApiResult<T>.getDataOrNull(): T? {
    return when (this) {
        is SafeApiResult.Success -> data
        else -> null
    }
}