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
                navigateToUserDetails = { userLogin ->
                    navController.navigate(Screen.RepositoriesScreen.createRoute(userLogin = userLogin))
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
            val userLogin = backStackEntry.arguments?.getString("login") ?: "andreesperanca"
            RepositoriesScreenRoot(
                userLogin = userLogin,
                navigateToSearchRepositories = { userLogin ->
                    navController.navigate(Screen.SearchRepositoriesScreen.createRoute(userLogin = userLogin))
                },
                navigateToRepositoryDetails = {

                },
                backButtonListener = { navController.popBackStack() }
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
            val login = backStackEntry.arguments?.getString("login") ?: "andreesperanca"
            SearchRepositoriesScreenRoot(
                userLogin = login
            )
        }
    }
}