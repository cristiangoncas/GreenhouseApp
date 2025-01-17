package com.cristiangoncas.greenhousemonitor.di

import com.cristiangoncas.data.repository.LogsRepository
import com.cristiangoncas.data.repository.LogsRepositoryImpl
import org.koin.dsl.module

val frameworkModule = module {
    single<LogsRepository> { LogsRepositoryImpl(get(), get(), get()) }
}