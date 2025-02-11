package com.cristiangoncas.usecases.di

import com.cristiangoncas.usecases.Average12hUseCase
import com.cristiangoncas.usecases.Average24hUseCase
import com.cristiangoncas.usecases.Average48hUseCase
import com.cristiangoncas.usecases.FetchLastLogsUseCase
import com.cristiangoncas.usecases.HeaterEvents24hUseCase
import com.cristiangoncas.usecases.NextHeartbeatUseCase
import com.cristiangoncas.usecases.RequestHealthCheckUseCase
import com.cristiangoncas.usecases.ResetDefaultParamsUseCase
import com.cristiangoncas.usecases.SetHeartbeatPeriodUseCase
import com.cristiangoncas.usecases.SetMaxTempUseCase
import com.cristiangoncas.usecases.SetMinTempUseCase
import com.cristiangoncas.usecases.SetMorningTimeUseCase
import com.cristiangoncas.usecases.SetNightTempDifferenceUseCase
import com.cristiangoncas.usecases.SetNightTimeUseCase
import org.koin.dsl.module

val useCasesModule = module {
    factory { Average12hUseCase(get()) }
    factory { Average24hUseCase(get()) }
    factory { Average48hUseCase(get()) }
    single { FetchLastLogsUseCase(get()) }
    factory { HeaterEvents24hUseCase(get()) }
    factory { NextHeartbeatUseCase(get()) }
    factory { RequestHealthCheckUseCase(get()) }
    factory { ResetDefaultParamsUseCase(get()) }
    factory { SetHeartbeatPeriodUseCase(get()) }
    factory { SetMaxTempUseCase(get()) }
    factory { SetMinTempUseCase(get()) }
    factory { SetMorningTimeUseCase(get()) }
    factory { SetNightTimeUseCase(get()) }
    factory { SetNightTempDifferenceUseCase(get()) }
}