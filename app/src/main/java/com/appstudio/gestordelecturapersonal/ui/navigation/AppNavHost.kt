package com.appstudio.gestordelecturapersonal.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager
import com.appstudio.gestordelecturapersonal.domain.network.ConnectivityState
import com.appstudio.gestordelecturapersonal.domain.sync.SyncState
import com.appstudio.gestordelecturapersonal.ui.component.CustomSnackbar
import com.appstudio.gestordelecturapersonal.ui.component.NotificationType
import com.appstudio.gestordelecturapersonal.ui.component.OfflineBanner
import com.appstudio.gestordelecturapersonal.ui.component.SyncStatusIndicator
import com.appstudio.gestordelecturapersonal.ui.network.ConnectivityViewModel
import com.appstudio.gestordelecturapersonal.ui.screen.auth.AuthGateScreen
import com.appstudio.gestordelecturapersonal.ui.screen.auth.AuthGateViewModel
import com.appstudio.gestordelecturapersonal.ui.screen.books.form.BookFormScreen
import com.appstudio.gestordelecturapersonal.ui.screen.login.LoginScreen
import com.appstudio.gestordelecturapersonal.ui.screen.books.list.BooksScreen
import com.appstudio.gestordelecturapersonal.ui.screen.notes.NotesScreen
import com.appstudio.gestordelecturapersonal.ui.screen.notes.form.NoteFormScreen
import com.appstudio.gestordelecturapersonal.ui.screen.recoverpassword.RecoverPasswordScreen
import com.appstudio.gestordelecturapersonal.ui.screen.register.RegisterScreen
import com.appstudio.gestordelecturapersonal.ui.screen.settings.SettingsScreen
import com.appstudio.gestordelecturapersonal.ui.screen.settings.SettingsViewModel
import com.appstudio.gestordelecturapersonal.ui.screen.settings.SettingsViewModelFactory
import com.appstudio.gestordelecturapersonal.ui.screen.settings.ThemePreferences
import com.appstudio.gestordelecturapersonal.ui.screen.statistics.StatisticsScreen
import com.appstudio.gestordelecturapersonal.ui.sync.SyncViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    syncManager: SyncManager? = null,
    authGateViewModel: AuthGateViewModel,
    themePrefs: ThemePreferences
) {

    val syncViewModel: SyncViewModel = viewModel()
    val syncState by syncViewModel.syncState.collectAsState()

    val connectivityViewModel: ConnectivityViewModel = viewModel()
    val connectivityState by connectivityViewModel.connectivityState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var activeType by remember { mutableStateOf<NotificationType>(NotificationType.Online) }

    val showMsg: (String, NotificationType) -> Unit = { message, type ->
        activeType = type
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(connectivityState) {
        when (connectivityState) {
            ConnectivityState.OFFLINE_NO_NETWORK, ConnectivityState.OFFLINE_FORCED -> {
                showMsg("Sin conexión a internet", NotificationType.Offline)
            }
            ConnectivityState.ONLINE -> {
                showMsg("En línea", NotificationType.Online)
            }
            else -> {}
        }
    }

    LaunchedEffect(syncState) {
        when (syncState) {
            SyncState.SYNCING, -> {
                showMsg("Sincronizando datos", NotificationType.Syncing)
            }
            SyncState.ERROR -> {
                showMsg("Error de sincronización", NotificationType.Offline)
            }
            else -> {}
        }
    }

    Scaffold (
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 16.dp)
            ) { data ->
                CustomSnackbar(
                    type = activeType,
                    message = data.visuals.message
                )
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
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
                        },
                        viewModel = authGateViewModel
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
                        },
                        onShowSnackbar = showMsg
                    )
                }

                // ---------- REGISTER ----------
                composable(AppRoutes.Register.route) {
                    RegisterScreen(
                        onRegisterSuccess = {
                            navController.popBackStack()
                        },
                        onShowSnackbar = showMsg
                    )
                }

                // ---------- RECOVERY PASSWORD ----------
                composable(AppRoutes.RecoverPassword.route) {
                    RecoverPasswordScreen(
                        onBackToLogin = {
                            navController.popBackStack()
                        },
                        onShowSnackbar = showMsg
                    )
                }

                // ---------- BOOKS ----------
                composable(AppRoutes.Books.route) {
                    BooksScreen(
                        navController = navController,
                        syncManager = syncManager,
                        onShowSnackbar = showMsg
                    )
                }

                composable(AppRoutes.AddBook.route) {
                    BookFormScreen(
                        navController = navController,
                        onBackPage = {
                            navController.popBackStack()
                        },
                        syncManager = syncManager,
                        onShowSnackbar = showMsg
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
                        syncManager = syncManager,
                        onShowSnackbar = showMsg
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
                        syncManager = syncManager,
                        onShowSnackbar = showMsg
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
                        syncManager = syncManager,
                        onShowSnackbar = showMsg
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
                        syncManager = syncManager,
                        onShowSnackbar = showMsg
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
                        },
                        onSnackbarShow = showMsg,
                        themePrefs = themePrefs
                    )
                }
            }
        }
    }
}

