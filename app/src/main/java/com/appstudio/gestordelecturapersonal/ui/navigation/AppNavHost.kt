package com.appstudio.gestordelecturapersonal.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager
import com.appstudio.gestordelecturapersonal.ui.component.OfflineBanner
import com.appstudio.gestordelecturapersonal.ui.component.SyncStatusIndicator
import com.appstudio.gestordelecturapersonal.ui.network.ConnectivityViewModel
import com.appstudio.gestordelecturapersonal.ui.screen.auth.AuthGateScreen
import com.appstudio.gestordelecturapersonal.ui.screen.books.form.BookFormScreen
import com.appstudio.gestordelecturapersonal.ui.screen.login.LoginScreen
import com.appstudio.gestordelecturapersonal.ui.screen.books.list.BooksScreen
import com.appstudio.gestordelecturapersonal.ui.screen.notes.NotesScreen
import com.appstudio.gestordelecturapersonal.ui.screen.notes.form.NoteFormScreen
import com.appstudio.gestordelecturapersonal.ui.screen.recoverpassword.RecoverPasswordScreen
import com.appstudio.gestordelecturapersonal.ui.screen.register.RegisterScreen
import com.appstudio.gestordelecturapersonal.ui.screen.settings.SettingsScreen
import com.appstudio.gestordelecturapersonal.ui.screen.statistics.StatisticsScreen
import com.appstudio.gestordelecturapersonal.ui.sync.SyncViewModel

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    syncManager: SyncManager? = null
) {

    val syncViewModel: SyncViewModel = viewModel()
    val syncState by syncViewModel.syncState.collectAsState()

    val connectivityViewModel: ConnectivityViewModel = viewModel()
    val connectivityState by connectivityViewModel.connectivityState.collectAsState()

    Column {

        OfflineBanner(state = connectivityState)
        SyncStatusIndicator(state = syncState)

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
                    navController = navController,
                    syncManager = syncManager
                )
            }

            composable(AppRoutes.AddBook.route) {
                BookFormScreen(
                    navController = navController,
                    onBackPage = {
                        navController.popBackStack()
                    },
                    syncManager = syncManager
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
                    },
                    syncManager = syncManager
                )
            }

            // ---------- NOTES ----------
            composable(
                route = AppRoutes.Notes.route,
                arguments = listOf(navArgument("bookId") { type = NavType.LongType })
            ) {
                val bookId = it.arguments!!.getLong("bookId")
                NotesScreen(
                    navController = navController,
                    bookId = bookId,
                    onBackPage = {
                        navController.popBackStack()
                    },
                    syncManager = syncManager
                )
            }

            composable(
                route = AppRoutes.AddNote.route,
                arguments = listOf(navArgument("bookId") { type = NavType.LongType })
            ) {
                val bookId = it.arguments!!.getLong("bookId")
                NoteFormScreen(
                    navController = navController,
                    bookId = bookId,
                    onBackPage = {
                        navController.popBackStack()
                    },
                    syncManager = syncManager
                )
            }

            composable(
                route = AppRoutes.EditNote.route,
                arguments = listOf(
                    navArgument("bookId") { type = NavType.LongType },
                    navArgument("noteId") { type = NavType.LongType }
                )
            ) {
                val bookId = it.arguments!!.getLong("bookId")
                val noteId = it.arguments!!.getLong("noteId")
                NoteFormScreen(
                    navController = navController,
                    bookId = bookId,
                    noteId = noteId,
                    onBackPage = {
                        navController.popBackStack()
                    },
                    syncManager = syncManager
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
}

