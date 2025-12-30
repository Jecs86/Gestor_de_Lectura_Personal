package com.appstudio.gestordelecturapersonal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appstudio.gestordelecturapersonal.ui.screen.login.LoginScreen
import com.appstudio.gestordelecturapersonal.ui.screen.books.BooksScreen
import com.appstudio.gestordelecturapersonal.ui.screen.settings.SettingsScreen
import com.appstudio.gestordelecturapersonal.ui.screen.statistics.StatisticsScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.Login.route
    ) {

        composable(AppRoutes.Login.route) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(AppRoutes.Books.route) {
                        popUpTo(AppRoutes.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    // FUTURO
                },
                onForgotPasswordClick = {
                    // FUTURO
                }
            )
        }

        composable(AppRoutes.Books.route) {
            BooksScreen(navController)
        }

        composable(AppRoutes.Statistics.route) {
            StatisticsScreen(navController)
        }

        composable(AppRoutes.Settings.route) {
            SettingsScreen(navController)
        }
    }
}

