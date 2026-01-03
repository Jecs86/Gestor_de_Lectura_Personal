package com.appstudio.gestordelecturapersonal.domain.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ConnectivityStatusHolder {

    private val _state = MutableStateFlow(ConnectivityState.ONLINE)
    val state: StateFlow<ConnectivityState> = _state

    fun setOnline() {
        _state.value = ConnectivityState.ONLINE
    }

    fun setOfflineForced() {
        _state.value = ConnectivityState.OFFLINE_FORCED
    }

    fun setOfflineNoNetwork() {
        _state.value = ConnectivityState.OFFLINE_NO_NETWORK
    }

    fun isOffline(): Boolean {
        return _state.value != ConnectivityState.ONLINE
    }

    fun isForcedOffline(): Boolean {
        return _state.value != ConnectivityState.OFFLINE_FORCED
    }
}