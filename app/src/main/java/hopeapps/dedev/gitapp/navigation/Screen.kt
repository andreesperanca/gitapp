package hopeapps.dedev.gitapp.navigation

sealed class Screen(val route: String){
    object RepositoriesScreen: Screen("repositories_screen")
    object SearchRepositoriesScreen: Screen("search_repositories_screen")
}
