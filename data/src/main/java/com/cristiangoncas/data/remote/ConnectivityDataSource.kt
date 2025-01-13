package com.cristiangoncas.data.remote

import kotlinx.coroutines.flow.StateFlow

interface ConnectivityDataSource {

    val isConnected: StateFlow<Boolean>
}