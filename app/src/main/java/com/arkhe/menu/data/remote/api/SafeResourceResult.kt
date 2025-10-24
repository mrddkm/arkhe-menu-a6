package com.arkhe.menu.data.remote.api

sealed class SafeResourceResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : SafeResourceResult<T>(data)
    class Failure<T>(message: String, data: T? = null) : SafeResourceResult<T>(data, message)
    class Loading<T>(data: T? = null) : SafeResourceResult<T>(data)
}
