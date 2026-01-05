package com.appstudio.gestordelecturapersonal.ui.screen.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appstudio.gestordelecturapersonal.data.local.db.AppDatabase
import com.appstudio.gestordelecturapersonal.data.repository.SyncRepository

class SettingsViewModelFactory(
    private val database: AppDatabase,
    private val syncRepository: SyncRepository,
    private val themPrefs: ThemePreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Pasamos el contexto al ViewModel
            return SettingsViewModel(database,syncRepository, themPrefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}