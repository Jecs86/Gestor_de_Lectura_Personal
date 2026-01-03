package com.appstudio.gestordelecturapersonal.ui.network

import androidx.lifecycle.ViewModel
import com.appstudio.gestordelecturapersonal.domain.network.ConnectivityStatusHolder
import com.appstudio.gestordelecturapersonal.domain.network.ConnectivityState
import kotlinx.coroutines.flow.StateFlow

class ConnectivityViewModel : ViewModel() {

    val connectivityState: StateFlow<ConnectivityState> =
        ConnectivityStatusHolder.state

    fun forceOffline() {
        ConnectivityStatusHolder.setOfflineForced()
    }

    fun goOnline() {
        ConnectivityStatusHolder.setOnline()
    }
}