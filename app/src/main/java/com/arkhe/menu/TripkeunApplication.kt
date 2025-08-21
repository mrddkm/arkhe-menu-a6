package com.arkhe.menu

import android.app.Application
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TripkeunApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TripkeunApplication)
            modules(
                appModule,
                dataModule,
                domainModule
            )
        }
    }
}
