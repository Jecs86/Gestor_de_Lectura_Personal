package com.appstudio.gestordelecturapersonal.ui.navigation

sealed class AppRoutes(val route: String) {

    object AuthGate : AppRoutes("auth_gate")
    object Login : AppRoutes("login")
    object Register : AppRoutes("register")
    object Books : AppRoutes("books")
    object Statistics : AppRoutes("statistics")
    object Settings : AppRoutes("settings")
}
