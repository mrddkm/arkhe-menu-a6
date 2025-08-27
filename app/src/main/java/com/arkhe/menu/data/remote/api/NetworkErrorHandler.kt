package com.arkhe.menu.data.remote.api

import com.arkhe.menu.domain.model.NetworkException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object NetworkErrorHandler {

    suspend fun handleException(exception: Throwable): NetworkException {
        return when (exception) {
            is ConnectException,
            is UnknownHostException,
            is ConnectTimeoutException -> NetworkException.NoInternetConnection

            is SocketTimeoutException,
            is HttpRequestTimeoutException -> NetworkException.RequestTimeout

            is ResponseException -> {
                when (exception.response.status.value) {
                    401 -> NetworkException.Unauthorized
                    in 500..599 -> NetworkException.ServerError
                    else -> NetworkException.HttpError(
                        code = exception.response.status.value,
                        message = exception.response.status.description
                    )
                }
            }

            is SerializationException -> NetworkException.ParseError(
                message = "Failed to parse server response"
            )

            else -> NetworkException.Unknown(
                message = exception.message ?: "Unknown network error"
            )
        }
    }

    fun getErrorMessage(exception: NetworkException): String {
        return when (exception) {
            NetworkException.NoInternetConnection ->
                "No internet connection. Please check your network and try again."
            NetworkException.RequestTimeout ->
                "Request timed out. Please try again."
            NetworkException.ServerError ->
                "Server error occurred. Please try again later."
            NetworkException.Unauthorized ->
                "Session expired. Please login again."
            is NetworkException.HttpError ->
                "Network error: ${exception.message}"
            is NetworkException.ParseError ->
                "Data parsing error: ${exception.message}"
            is NetworkException.Unknown ->
                "Unexpected error: ${exception.message}"
        }
    }
}