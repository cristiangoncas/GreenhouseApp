package com.cristiangoncas.greenhousemonitor

import android.app.Application
import com.cristiangoncas.data.di.dataModule
import com.cristiangoncas.greenhousemonitor.di.frameworkModule
import com.cristiangoncas.greenhousemonitor.di.uiModule
import com.cristiangoncas.usecases.di.useCasesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(appModule, dataModule, useCasesModule, uiModule, frameworkModule)
        }
    }
}

val appModule = module {
    single(named("apiKey")) { BuildConfig.API_IP }
}
