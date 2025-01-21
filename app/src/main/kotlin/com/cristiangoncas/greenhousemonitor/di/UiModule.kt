package com.cristiangoncas.greenhousemonitor.di

import com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat.HeartbeatViewModel
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeViewModel
import com.cristiangoncas.greenhousemonitor.ui.screen.logs.LogsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { LogsViewModel(get()) }
    viewModel { HeartbeatViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}
