package hopeapps.dedev.gitapp.navigation

sealed class Screen(val route: String) {

    object UserScreen : Screen("user_screen")

    object RepositoriesScreen : Screen("repositories_screen/{login}") {
        fun createRoute(userLogin: String) =
            "repositories_screen/$userLogin"
    }

    object SearchRepositoriesScreen : Screen("search_repositories_screen/{login}") {
        fun createRoute(userLogin: String) =
            "search_repositories_screen/$userLogin"
    }

    object RepoDetailsScreen : Screen("repo_details_screen/{repoId}") {
        fun createRoute(repoId: Long) =
            "repo_details_screen/$repoId"
    }

}