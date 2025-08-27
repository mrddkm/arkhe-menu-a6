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

    @Suppress("KotlinConstantConditions")
    fun handleException(exception: Throwable): NetworkException {
        return when (exception) {
            is ConnectException,
            is UnknownHostException,
            is ConnectTimeoutException -> NetworkException.NoInternetConnection

            is SocketTimeoutException,
            is HttpRequestTimeoutException -> NetworkException.RequestTimeout

            is ResponseException -> {
                when (exception.response.status.value) {
                    302 -> {
                        // Handle Google Apps Script redirects
                        NetworkException.ParseError("API returned redirect instead of JSON. Check Google Apps Script configuration.")
                    }
                    401 -> NetworkException.Unauthorized
                    in 500..599 -> NetworkException.ServerError
                    else -> NetworkException.HttpError(
                        code = exception.response.status.value,
                        message = exception.response.status.description
                    )
                }
            }

            is SerializationException -> {
                val message = when {
                    exception.message?.contains("Expected response body of the type") == true -> {
                        "API returned HTML instead of JSON. This usually means:\n" +
                                "1. Google Apps Script URL is incorrect\n" +
                                "2. Script is not deployed as web app\n" +
                                "3. Script permissions are not set correctly\n" +
                                "4. Request format is not what the script expects"
                    }
                    exception.message?.contains("NoTransformationFoundException") == true -> {
                        "Cannot parse API response. Server returned unexpected format."
                    }
                    else -> "Failed to parse server response: ${exception.message}"
                }
                NetworkException.ParseError(message)
            }

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
                "Network error (${exception.code}): ${exception.message}"
            is NetworkException.ParseError ->
                exception.message
            is NetworkException.Unknown ->
                "Unexpected error: ${exception.message}"
        }
    }
}