package com.cristiangoncas.greenhousemonitor.ui.common

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.cristiangoncas.data.remote.ConnectivityDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow

class ConnectivityState(
    private val connectivityManager: ConnectivityManager
) : ConnectivityDataSource {

    override val isConnected = MutableStateFlow(connectivityManager.isCurrentlyConnected())

    init {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isConnected.value = true
            }

            override fun onLost(network: Network) {
                isConnected.value = false
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        Runtime.getRuntime().addShutdownHook(Thread {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        })
    }

    private fun ConnectivityManager.isCurrentlyConnected(): Boolean {
        val activeNetwork = activeNetwork ?: return false
        val networkCapabilities = getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
