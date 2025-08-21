@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.arkhe.menu.data.local.TripkeunDatabase
import com.arkhe.menu.data.remote.TripkeunApiService
import com.arkhe.menu.data.repository.TripkeunRepositoryImpl
import com.arkhe.menu.domain.repository.TripkeunRepository
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tripkeun_settings")

val dataModule = module {
    // HttpClient
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
                level = LogLevel.ALL
            }
        }
    }

    // Database
    single<TripkeunDatabase> {
        Room.databaseBuilder(
                androidContext(),
                TripkeunDatabase::class.java,
                "tripkeun_database"
            ).fallbackToDestructiveMigration(false)
            .build()
    }

    // DAOs
    single { get<TripkeunDatabase>().tripkeunDao() }
    single { get<TripkeunDatabase>().receiptDao() }

    // DataStore
    single { androidContext().dataStore }

    // API Service
    single { TripkeunApiService(get()) }

    // Repository
    single<TripkeunRepository> {
        TripkeunRepositoryImpl(
            tripkeunDao = get(),
            receiptDao = get(),
            apiService = get(),
            dataStore = get()
        )
    }
}