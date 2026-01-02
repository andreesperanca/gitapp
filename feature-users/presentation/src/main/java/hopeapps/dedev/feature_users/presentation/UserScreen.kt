package hopeapps.dedev.feature_users.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hopeapps.dedev.core.presentation.designsystem.LocalSpacing
import hopeapps.dedev.core.presentation.designsystem.components.DefaultSearchBar
import hopeapps.dedev.core.presentation.designsystem.components.DefaultTitle
import hopeapps.dedev.core.presentation.designsystem.components.EmptyState
import hopeapps.dedev.core.presentation.designsystem.components.UserDetailsItem
import hopeapps.dedev.core.presentation.designsystem.screen.LoadingLayout
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserScreenRoot(
    viewModel: UserViewModel = koinViewModel(),
    navigateToUserDetails: (String) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is UserEvent.UserSelected -> {
                    navigateToUserDetails(event.user.login)
                }

                is UserEvent.ShowSnackBar -> {
                    scope.launch {
                        snackBarHostState.showSnackbar(message = event.message)
                    }
                }
            }
        }
    }

    UserScreen(
        userState = state,
        onAction = viewModel::onAction,
        snackBarHostState = snackBarHostState
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    userState: UserState,
    onAction: (UserAction) -> Unit,
    snackBarHostState: SnackbarHostState
) {

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->
        LoadingLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            isLoading = userState.isLoading,
            content = {
                Column(
                    modifier = Modifier
                ) {

                    DefaultSearchBar(
                        modifier = Modifier
                            .padding(
                                start = LocalSpacing.current.medium,
                                top = 32.dp,
                                end = LocalSpacing.current.medium,
                                bottom = LocalSpacing.current.small
                            ),
                        searchQuery = userState.filterText,
                        onQueryChange = { newFilterText ->
                            onAction(UserAction.UpdateFilterText(newFilterText))
                        },
                        isExpanded = userState.isSearchExpanded,
                        onExpandedChange = { isExpanded ->
                            onAction(UserAction.ExpandedSearchClick(isExpanded))
                        },
                        filterListener = { filterText ->
                            onAction(UserAction.FilterClick(filterText))
                        },
                        recentSearch = userState.recentUsers.map { user -> user.login },
                        placeholder = stringResource(R.string.search_users),
                        onClickSuggestItem = { suggestItem ->
                            onAction(UserAction.UpdateFilterText(suggestItem))
                            onAction(UserAction.FilterClick(suggestItem))
                        }
                    )

                    DefaultTitle(
                        modifier = Modifier
                            .padding(
                                vertical = LocalSpacing.current.medium,
                                horizontal = LocalSpacing.current.medium
                            ),
                        title = stringResource(R.string.recent_users),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement
                            .spacedBy(LocalSpacing.current.extraSmall)
                    ) {
                        item {
                            if (userState.recentUsers.isEmpty()) {
                                EmptyState(
                                    modifier = Modifier.padding(
                                        top = LocalSpacing.current.large
                                    ),
                                    message = stringResource(R.string.is_empty),
                                    description = stringResource(R.string.search_users)
                                )
                            }
                        }

                        items(userState.recentUsers) { item ->
                            UserDetailsItem(
                                modifier = Modifier.padding(
                                    horizontal = LocalSpacing.current.medium
                                ),
                                name = item.name,
                                bio = item.bio,
                                follows = item.followers.toString(),
                                following = item.following.toString(),
                                repositories = item.publicRepos.toString(),
                                avatarUrl = item.avatarUrl,
                                contentDescription = item.bio,
                                onClickItem = {
                                    onAction(UserAction.SelectedUser(item))
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}


@Composable
@Preview(showBackground = true)
fun UserScreenPreview() {
    UserScreen(
        userState = UserState(),
        onAction = {

        },
        snackBarHostState = SnackbarHostState()
    )
}

