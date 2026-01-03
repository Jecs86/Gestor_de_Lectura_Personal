package com.appstudio.gestordelecturapersonal.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

class SyncManager(
    private val syncRepository: SyncRepository,
    private val coroutineScope: CoroutineScope
) {

    private val syncEvents = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1
    )

    init {
        coroutineScope.launch {
            syncEvents
                .collectLatest {
                    delay(3_000) // ⏱️ debounce 3 segundos

                    val currentUid = FirebaseAuth.getInstance().currentUser?.uid

                    if (currentUid != null) {
                        syncRepository.syncPendingChanges(currentUid)
                        syncRepository.syncPendingDeletes(currentUid)
                    }

                }
        }
    }

    fun notifyChange() {
        syncEvents.tryEmit(Unit)
    }
}