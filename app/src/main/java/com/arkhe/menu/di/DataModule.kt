package com.arkhe.menu.di

import android.util.Log
import androidx.room.Room
import com.arkhe.menu.data.local.LocalDataSource
import com.arkhe.menu.data.local.database.AppDatabase
import com.arkhe.menu.data.local.preferences.SessionManager
import com.arkhe.menu.data.local.preferences.dataStore
import com.arkhe.menu.data.remote.RemoteDataSource
import com.arkhe.menu.data.remote.api.TripkeunApiService
import com.arkhe.menu.data.remote.api.TripkeunApiServiceImpl
import com.arkhe.menu.data.repository.CategoryRepositoryImpl
import com.arkhe.menu.data.repository.ProfileRepositoryImpl
import com.arkhe.menu.domain.repository.CategoryRepository
import com.arkhe.menu.domain.repository.ProfileRepository
import com.arkhe.menu.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    /*DataStore*/
    single { androidContext().dataStore }
    single { SessionManager(get()) }

    /*Ktor HTTP Client - OkHttp Engine for GAS*/
    single<HttpClient> {
        HttpClient(OkHttp) {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = Constants.URL_HOST
                }
                headers.append(HttpHeaders.UserAgent, Constants.HTTP_HEADER_USER_AGENT)
                headers.append(HttpHeaders.Accept, "application/json")
                headers.append(HttpHeaders.CacheControl, "no-cache")
                contentType(ContentType.Application.Json)
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                    encodeDefaults = true
                })
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("KtorClient", message)
                    }
                }
                sanitizeHeader { header -> header == "Authorization" }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 60000
                connectTimeoutMillis = 60000
                socketTimeoutMillis = 60000
            }

            expectSuccess = false
            followRedirects = true

            /*OkHttp specific configuration - redirect handled by engine*/
            engine {
                config {
                    followRedirects(true)
                    followSslRedirects(true)
                    retryOnConnectionFailure(true)
                    /*Timeout configuration*/
                    connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                }
            }
        }
    }

    /*Room Database*/
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    /*Room DAOs*/
    single { get<AppDatabase>().profileDao() }
    single { get<AppDatabase>().categoryDao() }

    /*API Services*/
    single<TripkeunApiService> { TripkeunApiServiceImpl(get()) }

    /*Data Sources*/
    single { RemoteDataSource(get()) }
    single { LocalDataSource(get(), get()) }

    /*Repositories*/
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get(), get()) }
}