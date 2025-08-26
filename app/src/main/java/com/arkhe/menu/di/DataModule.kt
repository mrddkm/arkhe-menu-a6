@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.di

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
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    // DataStore
    single { androidContext().dataStore }
    single { SessionManager(get()) }

    // Ktor HTTP Client
    single<HttpClient> {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(Logging) {
                level = LogLevel.BODY
                logger = Logger.DEFAULT
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
                socketTimeoutMillis = 30000
            }

            install(DefaultRequest) {
                headers.append("Content-Type", "application/json")
                headers.append("Accept", "application/json")
            }

            // Add error handling
            expectSuccess = false
        }
    }

    // Room Database
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    // Room DAOs
    single { get<AppDatabase>().profileDao() }

    // API Services
    single<TripkeunApiService> { TripkeunApiServiceImpl(get()) }

    // Data Sources
    single { RemoteDataSource(get()) }
    single { LocalDataSource(get()) }

    // Repositories
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
}