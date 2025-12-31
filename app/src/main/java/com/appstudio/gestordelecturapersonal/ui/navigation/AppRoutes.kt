package com.appstudio.gestordelecturapersonal.ui.navigation

sealed class AppRoutes(val route: String) {

    object AuthGate : AppRoutes("auth_gate")
    object Login : AppRoutes("login")
    object Register : AppRoutes("register")
    object RecoverPassword : AppRoutes("recover_password")
    object Books : AppRoutes("books")
    object EditBook : AppRoutes("edit_book/{bookId}") {
        fun createRoute(bookId: Int) = "edit_book/$bookId"
    }
    object AddBook : AppRoutes("add_book")
    object Statistics : AppRoutes("statistics")
    object Settings : AppRoutes("settings")
}
