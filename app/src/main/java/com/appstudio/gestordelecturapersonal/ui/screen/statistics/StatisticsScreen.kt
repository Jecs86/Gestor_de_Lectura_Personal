package com.appstudio.gestordelecturapersonal.ui.screen.statistics

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.ui.component.AppBottomBar

@Composable
fun StatisticsScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            StatisticsTopBar()
        },
        bottomBar = { AppBottomBar(navController) }
    ) { paddingValues ->
        StatisticsContent(
            paddingValues = paddingValues
        )
    }
}
