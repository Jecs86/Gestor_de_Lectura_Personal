package com.appstudio.gestordelecturapersonal.ui.screen.settings

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.ui.component.AppBottomBar

@Composable
fun SettingsScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            SettingsTopBar()
        },
        bottomBar = { AppBottomBar(navController)}
    ) { paddingValues ->
        SettingsContent(
            paddingValues = paddingValues
        )
    }
}
