package com.appstudio.gestordelecturapersonal.data.repository

import android.util.Log
import com.appstudio.gestordelecturapersonal.domain.network.ConnectivityStatusHolder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import com.appstudio.gestordelecturapersonal.domain.sync.SyncStatusHolder

private const val MAX_RETRIES = 3

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

                    if(ConnectivityStatusHolder.isOffline()) {
                        return@collectLatest
                    }

                    var currentRetry = 0
                    var success = false

                    while (currentRetry < MAX_RETRIES && !success) {
                        try {

                            SyncStatusHolder.setSyncing()

                            val currentUid = FirebaseAuth.getInstance().currentUser?.uid

                            if (currentUid != null) {
                                syncRepository.syncPendingDeletes(currentUid)
                                syncRepository.syncPendingChanges(currentUid)

                                success = true
                                SyncStatusHolder.setIdle()
                            }
                        }catch (e: Exception) {
                            currentRetry++
                            Log.e("SYNC", "Fallo intento $currentRetry de $MAX_RETRIES: ${e.message}")

                            if (currentRetry < MAX_RETRIES){
                                val waitTime = (2000L * Math.pow(2.0, (currentRetry - 1).toDouble())).toLong()
                                delay(waitTime)
                            }else {
                                SyncStatusHolder.setError()
                            }
                        }
                    }
                }
        }
    }

    fun notifyChange() {
        syncEvents.tryEmit(Unit)
    }
}