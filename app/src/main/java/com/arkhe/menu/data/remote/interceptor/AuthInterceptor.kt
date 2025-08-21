package com.arkhe.menu.data.remote.interceptor

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import com.arkhe.menu.di.dataStore
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class AuthInterceptor(private val context: Context) {

    private val tokenKey = stringPreferencesKey("auth_token")

    fun HttpClientConfig<*>.installAuth() {
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)

            // Add authorization header if token exists
            runBlocking {
                val token = context.dataStore.data.map { preferences ->
                    preferences[tokenKey]
                }.first()

                token?.let {
                    header(HttpHeaders.Authorization, "Bearer $it")
                }
            }
        }
    }
}