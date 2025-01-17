package com.cristiangoncas.data.di

import com.cristiangoncas.data.local.LocalDataSource
import com.cristiangoncas.data.remote.ConnectivityDataSource
import com.cristiangoncas.data.remote.RemoteDataSource
import com.cristiangoncas.data.repository.HeartbeatRepository
import com.cristiangoncas.data.repository.HeartbeatRepositoryImpl
import com.cristiangoncas.data.repository.LogsRepository
import com.cristiangoncas.data.repository.LogsRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single<RemoteDataSource> { (get()) }
    single<LocalDataSource> { (get()) }
    single<ConnectivityDataSource> { (get()) }
    single<LogsRepository> { LogsRepositoryImpl(remoteDataSource = get(), localDataSource = get(), connectivityState =  get()) }
    single<HeartbeatRepository> { HeartbeatRepositoryImpl(get()) }
}