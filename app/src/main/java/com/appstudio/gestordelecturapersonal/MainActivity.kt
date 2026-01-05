package com.appstudio.gestordelecturapersonal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.appstudio.gestordelecturapersonal.data.local.db.DatabaseProvider
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager
import com.appstudio.gestordelecturapersonal.data.repository.SyncRepository
import com.appstudio.gestordelecturapersonal.domain.network.NetworkMonitor
import com.appstudio.gestordelecturapersonal.ui.navigation.AppNavHost
import com.appstudio.gestordelecturapersonal.ui.screen.auth.AuthGateViewModel
import com.appstudio.gestordelecturapersonal.ui.screen.settings.ThemeMode
import com.appstudio.gestordelecturapersonal.ui.screen.settings.ThemePreferences
import com.appstudio.gestordelecturapersonal.ui.theme.GestorDeLecturaPersonalTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val themePrefs = ThemePreferences(this)

        FirebaseApp.initializeApp(this)

        val authGateViewModel: AuthGateViewModel by viewModels()
        splashScreen.setKeepOnScreenCondition {
            authGateViewModel.isAuthenticated.value == null
        }

        val database = DatabaseProvider.getDatabase(this)
        val firestore = FirebaseFirestore.getInstance()

        val syncRepository = SyncRepository(
            db = database,
            firestore = firestore
        )

        val syncManager = SyncManager(
            syncRepository = syncRepository,
            coroutineScope = lifecycleScope
        )

        NetworkMonitor(this, syncManager).start()

        setContent {

            val themeMode by themePrefs.themeMode.collectAsState(initial = ThemeMode.SYSTEM)

            val useDarkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            GestorDeLecturaPersonalTheme(darkTheme = useDarkTheme) {
                AppNavHost(
                    syncManager = syncManager,
                    authGateViewModel = authGateViewModel,
                    themePrefs = themePrefs
                )
            }
        }
    }
}