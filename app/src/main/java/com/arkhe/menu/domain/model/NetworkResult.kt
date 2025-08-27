package com.arkhe.menu.domain.model

sealed class NetworkException : Exception() {
    object NoInternetConnection : NetworkException() {
        private fun readResolve(): Any = NoInternetConnection
    }

    object RequestTimeout : NetworkException() {
        private fun readResolve(): Any = RequestTimeout
    }

    object ServerError : NetworkException() {
        private fun readResolve(): Any = ServerError
    }

    object Unauthorized : NetworkException() {
        private fun readResolve(): Any = Unauthorized
    }

    data class HttpError(val code: Int, override val message: String) : NetworkException()
    data class ParseError(override val message: String) : NetworkException()
    data class Unknown(override val message: String) : NetworkException()
}