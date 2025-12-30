package com.appstudio.gestordelecturapersonal.ui.navigation

sealed class Screen(val route: String) {

    object Login : Screen("login")

    object Books : Screen("books")

    object Statistics : Screen("statistics")

    object Settings : Screen("settings")
}
