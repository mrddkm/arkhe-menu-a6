package com.arkhe.menu.domain.model

sealed class NetworkException : Exception() {
    object NoInternetConnection : NetworkException()
    object RequestTimeout : NetworkException()
    object ServerError : NetworkException()
    object Unauthorized : NetworkException()
    data class HttpError(val code: Int, override val message: String) : NetworkException()
    data class ParseError(override val message: String) : NetworkException()
    data class Unknown(override val message: String) : NetworkException()
}