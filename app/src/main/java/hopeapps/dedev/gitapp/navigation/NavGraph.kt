package hopeapps.dedev.gitapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import hopeapps.dedev.feature_repo.presentation.list.RepositoriesScreenRoot
import hopeapps.dedev.feature_repo.presentation.search.SearchRepositoriesScreenRoot
import hopeapps.dedev.feature_users.presentation.UserScreenRoot


@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.UserScreen.route
    ) {

        composable(route = Screen.UserScreen.route) {
            UserScreenRoot(
                navigateToUserDetails = { user ->
                    navController.navigate(Screen.RepositoriesScreen.createRoute(loginUser = user))
                }
            )
        }

        composable(
            route = Screen.RepositoriesScreen.route,
            arguments = listOf(
                navArgument("login") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val login = backStackEntry.arguments?.getString("login") ?: error("login argument is required")

            RepositoriesScreenRoot(
                userLogin = login
            )
        }

        composable(
            route = Screen.SearchRepositoriesScreen.route,
            arguments = listOf(
                navArgument("login") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val login = backStackEntry.arguments?.getString("login")
                ?: error("login argument is required")

            SearchRepositoriesScreenRoot(
                userLogin = login
            )
        }
    }
}