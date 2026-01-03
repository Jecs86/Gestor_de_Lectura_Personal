package com.appstudio.gestordelecturapersonal.ui.screen.statistics

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.data.local.db.DatabaseProvider
import com.appstudio.gestordelecturapersonal.ui.component.AppBottomBar
import com.appstudio.gestordelecturapersonal.ui.component.AppTopBar

@Composable
fun StatisticsScreen(
    navController: NavController
) {

    val context = LocalContext.current

    val database = DatabaseProvider.getDatabase(context)

    val viewModel: StatisticsViewModel = viewModel(
        factory = StatisticsViewModelFactory(
            bookDao = database.bookDao()
        )
    )

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Estadísticas"
            )
        },
        bottomBar = { AppBottomBar(navController) }
    ) { paddingValues ->
        StatisticsContent(
            paddingValues = paddingValues,
            viewModel = viewModel
        )
    }
}
