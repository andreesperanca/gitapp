package hopeapps.dedev.gitapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import hopeapps.dedev.feature_repo.presentation.search.SearchRepositoriesScreenRoot
import hopeapps.dedev.feature_users.presentation.UserScreenRoot


@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.SearchRepositoriesScreen.route
    ) {
        composable(route = Screen.RepositoriesScreen.route){
            SearchRepositoriesScreenRoot()
        }
        composable(route = Screen.SearchRepositoriesScreen.route){
            SearchRepositoriesScreenRoot()
        }
    }
}