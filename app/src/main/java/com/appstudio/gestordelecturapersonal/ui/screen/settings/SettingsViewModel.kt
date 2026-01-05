package com.appstudio.gestordelecturapersonal.ui.screen.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstudio.gestordelecturapersonal.data.local.db.AppDatabase
import com.appstudio.gestordelecturapersonal.data.repository.SyncRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val database: AppDatabase,
    private val syncRepository: SyncRepository,
    private val themePrefs: ThemePreferences
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = themePrefs.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeMode.SYSTEM)

    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(
        SettingsUiState(email = auth.currentUser?.email)
    )
    val uiState: StateFlow<SettingsUiState> = _uiState

    fun setTheme(mode: ThemeMode) {
        viewModelScope.launch {
            themePrefs.saveThemeMode(mode)
        }
    }

    fun logout() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val uid = auth.currentUser?.uid

                if (uid != null) {
                    try {
                        syncRepository.syncToFirestore(uid)
                    } catch (e: Exception) {
                        Log.e("SYNC", "Error al sincronizar con Firestore al salir", e)
                    }
                }

                database.clearAllTables()

            } finally {
                withContext(Dispatchers.Main) {
                    auth.signOut()
                    _uiState.value = SettingsUiState(isLoggedOut = true)
                }
            }
        }
    }
}