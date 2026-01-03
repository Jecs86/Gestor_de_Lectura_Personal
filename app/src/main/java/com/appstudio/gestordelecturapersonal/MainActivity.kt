package com.appstudio.gestordelecturapersonal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.appstudio.gestordelecturapersonal.data.local.db.DatabaseProvider
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager
import com.appstudio.gestordelecturapersonal.data.repository.SyncRepository
import com.appstudio.gestordelecturapersonal.domain.network.NetworkMonitor
import com.appstudio.gestordelecturapersonal.ui.navigation.AppNavHost
import com.appstudio.gestordelecturapersonal.ui.theme.GestorDeLecturaPersonalTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        FirebaseApp.initializeApp(this)


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
            GestorDeLecturaPersonalTheme {
                AppNavHost(
                    syncManager = syncManager
                )
            }
        }
    }
}