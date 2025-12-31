package com.appstudio.gestordelecturapersonal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appstudio.gestordelecturapersonal.ui.auth.AuthGateScreen
import com.appstudio.gestordelecturapersonal.ui.auth.AuthGateViewModel
import com.appstudio.gestordelecturapersonal.ui.screen.login.LoginScreen
import com.appstudio.gestordelecturapersonal.ui.screen.books.BooksScreen
import com.appstudio.gestordelecturapersonal.ui.screen.register.RegisterScreen
import com.appstudio.gestordelecturapersonal.ui.screen.settings.SettingsScreen
import com.appstudio.gestordelecturapersonal.ui.screen.statistics.StatisticsScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.AuthGate.route
    ) {

        composable(AppRoutes.AuthGate.route) {
            AuthGateScreen(
                onAuthenticated = {
                    navController.navigate(AppRoutes.Books.route) {
                        popUpTo(AppRoutes.AuthGate.route) { inclusive = true }
                    }
                },
                onUnauthenticated = {
                    navController.navigate(AppRoutes.Login.route) {
                        popUpTo(AppRoutes.AuthGate.route) { inclusive = true }
                    }
                }
            )
        }

        // ---------- LOGIN ----------
        composable(AppRoutes.Login.route) {
            LoginScreen(
                navController = navController,
                onLoginSuccess = {
                    navController.navigate(AppRoutes.Books.route) {
                        popUpTo(AppRoutes.Login.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // ---------- REGISTER ----------
        composable(AppRoutes.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.popBackStack()
                }
            )
        }

        // ---------- BOOKS ----------
        composable(AppRoutes.Books.route) {
            BooksScreen(navController = navController)
        }

        // ---------- STATISTICS ----------
        composable(AppRoutes.Statistics.route) {
            StatisticsScreen(navController = navController)
        }

        // ---------- SETTINGS ----------
        composable(AppRoutes.Settings.route) {
            SettingsScreen(
                navController = navController,
                onLogoutSuccess = {
                    navController.navigate(AppRoutes.Login.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

