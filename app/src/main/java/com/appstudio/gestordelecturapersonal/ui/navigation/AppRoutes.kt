package com.appstudio.gestordelecturapersonal.ui.navigation

sealed class AppRoutes(val route: String) {

    object AuthGate : AppRoutes("auth_gate")
    object Login : AppRoutes("login")
    object Register : AppRoutes("register")
    object RecoverPassword : AppRoutes("recover_password")
    object Books : AppRoutes("books")
    object AddBook : AppRoutes("add_book")
    object EditBook : AppRoutes("edit_book/{bookId}") {
        fun createRoute(bookId: Int) = "edit_book/$bookId"
    }
    object Notes : AppRoutes("notes/{bookId}") {
        fun createRoute(bookId: Long) = "notes/$bookId"
    }
    object AddNote : AppRoutes("add_note/{bookId}") {
        fun createRoute(bookId: Long) = "add_note/$bookId"
    }
    object EditNote : AppRoutes("edit_note/{bookId}/{noteId}") {
        fun createRoute(bookId: Long, noteId: Long) =
            "edit_note/$bookId/$noteId"
    }
    object Statistics : AppRoutes("statistics")
    object Settings : AppRoutes("settings")
}
