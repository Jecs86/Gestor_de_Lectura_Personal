package com.appstudio.gestordelecturapersonal.ui.screen.statistics

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.ui.component.AppBottomBar
import com.appstudio.gestordelecturapersonal.ui.component.AppTopBar
import com.appstudio.gestordelecturapersonal.ui.component.AppTopBarPreview

@Composable
fun StatisticsScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Estadísticas"
            )
        },
        bottomBar = { AppBottomBar(navController) }
    ) { paddingValues ->
        StatisticsContent(
            paddingValues = paddingValues
        )
    }
}
