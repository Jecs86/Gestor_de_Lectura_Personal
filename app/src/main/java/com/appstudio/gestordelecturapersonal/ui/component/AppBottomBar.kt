package com.appstudio.gestordelecturapersonal.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.ui.navigation.AppRoutes
import com.appstudio.gestordelecturapersonal.ui.navigation.currentRoute
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Settings

@Composable
fun AppBottomBar(
    navController: NavController
) {
    val currentRoute = currentRoute(navController)

    val items = listOf(
        BottomNavItem(
            route = AppRoutes.Books.route,
            label = "Libros",
            icon = Icons.Default.Book
        ),
        BottomNavItem(
            route = AppRoutes.Statistics.route,
            label = "Estadísticas",
            icon = Icons.Default.BarChart
        ),
        BottomNavItem(
            route = AppRoutes.Settings.route,
            label = "Ajustes",
            icon = Icons.Default.Settings
        )
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(AppRoutes.Books.route)
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(item.label)
                }
            )
        }
    }
}
