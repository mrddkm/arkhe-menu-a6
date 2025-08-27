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
import com.arkhe.menu.data.repository.ProfileRepositoryImpl
import com.arkhe.menu.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    /*DataStore*/
    single { androidContext().dataStore }
    single { SessionManager(get()) }

    /*Ktor HTTP Client*/
    single<HttpClient> {
        HttpClient(CIO) {
            install(DefaultRequest) {
                contentType(ContentType.Application.Json)
                userAgent("TripkeunApp/1.0 (Android)")
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
            install(HttpRedirect) {
                checkHttpMethod = false
                allowHttpsDowngrade = false
            }
            expectSuccess = false
            followRedirects = true
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

    /*API Services*/
    single<TripkeunApiService> { TripkeunApiServiceImpl(get()) }

    /*Data Sources*/
    single { RemoteDataSource(get()) }
    single { LocalDataSource(get()) }

    /*Repositories*/
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
}