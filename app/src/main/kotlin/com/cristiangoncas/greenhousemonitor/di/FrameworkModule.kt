package com.cristiangoncas.greenhousemonitor.di

import android.content.Context
import android.net.ConnectivityManager
import com.cristiangoncas.data.local.LocalDataSource
import com.cristiangoncas.data.remote.ConnectivityDataSource
import com.cristiangoncas.data.remote.RemoteDataSource
import com.cristiangoncas.greenhousemonitor.framework.local.RoomDataSource
import com.cristiangoncas.greenhousemonitor.framework.local.database.GreenhouseDB
import com.cristiangoncas.greenhousemonitor.framework.local.database.LogEntryDao
import com.cristiangoncas.greenhousemonitor.framework.remote.APIDataSource
import com.cristiangoncas.greenhousemonitor.framework.remote.client.Api
import com.cristiangoncas.greenhousemonitor.framework.remote.client.ApiImpl
import com.cristiangoncas.greenhousemonitor.ui.common.ConnectivityState
import org.koin.core.qualifier.named
import org.koin.dsl.module

val frameworkModule = module {
    single<RemoteDataSource> { APIDataSource(get()) }
    single<LocalDataSource> { RoomDataSource(get()) }
    single<GreenhouseDB> { GreenhouseDB.getInstance(get()) }
    single<LogEntryDao> {
        val db: GreenhouseDB = get()
        db.logEntryDao()
    }
    single<ConnectivityDataSource> { ConnectivityState(get()) }
    single<ConnectivityManager> {
        val context: Context = get()
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    single<Api> { ApiImpl(apiUrl = get(named("apiKey"))) }
}