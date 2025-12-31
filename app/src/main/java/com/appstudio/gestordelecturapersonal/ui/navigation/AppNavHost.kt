package com.appstudio.gestordelecturapersonal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.appstudio.gestordelecturapersonal.ui.screen.auth.AuthGateScreen
import com.appstudio.gestordelecturapersonal.ui.screen.books.form.BookFormScreen
import com.appstudio.gestordelecturapersonal.ui.screen.login.LoginScreen
import com.appstudio.gestordelecturapersonal.ui.screen.books.list.BooksScreen
import com.appstudio.gestordelecturapersonal.ui.screen.recoverpassword.RecoverPasswordScreen
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

        // ---------- RECOVERY PASSWORD ----------
        composable(AppRoutes.RecoverPassword.route) {
            RecoverPasswordScreen(
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // ---------- BOOKS ----------
        composable(AppRoutes.Books.route) {
            BooksScreen(
                navController = navController
            )
        }

        composable(AppRoutes.AddBook.route) {
            BookFormScreen(
                navController = navController,
                onBackPage = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoutes.EditBook.route,
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) {
            val bookId = it.arguments?.getLong("bookId")
            BookFormScreen(
                navController = navController,
                bookId = bookId,
                onBackPage = {
                    navController.popBackStack()
                }
            )
        }

        // ---------- STATISTICS ----------
        composable(AppRoutes.Statistics.route) {
            StatisticsScreen(
                navController = navController
            )
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

