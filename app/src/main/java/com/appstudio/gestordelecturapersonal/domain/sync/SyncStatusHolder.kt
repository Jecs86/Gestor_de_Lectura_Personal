package com.appstudio.gestordelecturapersonal.domain.sync

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SyncStatusHolder {

    private val _syncState = MutableStateFlow(SyncState.IDLE)
    val syncState: StateFlow<SyncState> = _syncState

    fun setSyncing() {
        _syncState.value = SyncState.SYNCING
    }

    fun setIdle() {
        _syncState.value = SyncState.IDLE
    }

    fun setError() {
        _syncState.value = SyncState.ERROR
    }
}