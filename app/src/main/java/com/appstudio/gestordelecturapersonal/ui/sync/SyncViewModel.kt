package com.appstudio.gestordelecturapersonal.ui.sync

import androidx.lifecycle.ViewModel
import com.appstudio.gestordelecturapersonal.domain.sync.SyncStatusHolder
import com.appstudio.gestordelecturapersonal.domain.sync.SyncState
import kotlinx.coroutines.flow.StateFlow

class SyncViewModel : ViewModel() {

    val syncState: StateFlow<SyncState> =
        SyncStatusHolder.syncState
}