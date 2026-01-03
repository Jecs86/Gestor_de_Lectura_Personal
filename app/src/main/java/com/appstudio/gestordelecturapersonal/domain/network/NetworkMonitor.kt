package com.appstudio.gestordelecturapersonal.domain.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager

class NetworkMonitor(
    private val context: Context,
    private val syncManager: SyncManager
) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val callback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) { checkCurrentNetwork() }
        override fun onLost(network: Network) { checkCurrentNetwork() }
        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) { checkCurrentNetwork() }
    }

    fun start() {

        checkCurrentNetwork()

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)
    }

    private fun checkCurrentNetwork() {
        val nw = connectivityManager.activeNetwork
        val actNw = connectivityManager.getNetworkCapabilities(nw)
        val isConnected = actNw != null &&
                (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))

        // Solo actualizamos si el usuario NO activó el modo offline manualmente
        if (ConnectivityStatusHolder.state.value != ConnectivityState.OFFLINE_FORCED) {
            if (isConnected) {
                val wasOffline = ConnectivityStatusHolder.state.value != ConnectivityState.ONLINE
                ConnectivityStatusHolder.setOnline()

                if (wasOffline) {
                    syncManager.notifyChange()
                }
            } else {
                ConnectivityStatusHolder.setOfflineNoNetwork()
            }
        }
    }

}