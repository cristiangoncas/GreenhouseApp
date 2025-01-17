package com.cristiangoncas.greenhousemonitor.di

import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { HomeViewModel(get(), get()) }
}