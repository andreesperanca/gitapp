package hopeapps.dedev.gitapp.navigation

sealed class Screen(val route: String) {

    object UserScreen : Screen("user_screen")

    object RepositoriesScreen : Screen("repositories_screen/{login}") {
        fun createRoute(loginUser: String) =
            "repositories_screen/$loginUser"
    }

    object SearchRepositoriesScreen : Screen("search_repositories_screen/{login}") {
        fun createRoute(loginUser: String) =
            "search_repositories_screen/$loginUser"
    }
}