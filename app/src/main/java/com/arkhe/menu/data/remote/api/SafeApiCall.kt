package com.arkhe.menu.data.remote.api

import com.arkhe.menu.domain.model.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T
): ApiResult<T> {
    return withContext(Dispatchers.IO) {
        try {
            val result = apiCall()
            ApiResult.Success(result)
        } catch (exception: Exception) {
            val networkException = NetworkErrorHandler.handleException(exception)
            ApiResult.Error(networkException)
        }
    }
}