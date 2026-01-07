package hopeapps.dedev.feature_repo.presentation.list


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import hopeapps.dedev.core.presentation.designsystem.LocalSpacing
import hopeapps.dedev.core.presentation.designsystem.R
import hopeapps.dedev.core.presentation.designsystem.components.DefaultTopAppBar
import hopeapps.dedev.core.presentation.designsystem.components.EmptyState
import hopeapps.dedev.core.presentation.designsystem.components.ErrorState
import hopeapps.dedev.core.presentation.designsystem.components.RepositoryItem
import hopeapps.dedev.core.presentation.designsystem.screen.LoadingLayout
import hopeapps.dedev.feature_repo.domain.entity.Repository
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel


@Composable
fun RepositoriesScreenRoot(
    userLogin: String,
    viewModel: RepositoriesViewModel = koinViewModel(),
    navigateToSearchRepositories: (userLogin: String) -> Unit,
    navigateToRepositoryDetails: (repoId: Long) -> Unit,
    backButtonListener: () -> Unit
) {
    LaunchedEffect(true) {
        viewModel.init(userLogin)
    }
    val repositories = viewModel.repoPagingFlow.collectAsLazyPagingItems()

    RepositoriesScreen(
        repositories = repositories,
        onSearchClick = {
            navigateToSearchRepositories(userLogin)
        },
        onRepositoryClick = { repoId ->
            navigateToRepositoryDetails(repoId)
        },
        backButtonListener = {
            backButtonListener()
        }
    )
}


@Composable
fun RepositoriesScreen(
    repositories: LazyPagingItems<Repository>,
    onSearchClick: () -> Unit,
    onRepositoryClick: (repoId: Long) -> Unit,
    backButtonListener: () -> Unit
) {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.repositories),
                navigationIcon = {
                    IconButton(onClick = {
                        backButtonListener()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onSearchClick()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        LoadingLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            isLoading = repositories.loadState.refresh is LoadState.Loading,
            content = {
                Box ( modifier = Modifier.fillMaxSize() ) {

                    val listState = rememberLazyListState()
                    val hasItems = repositories.itemCount > 0

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = LocalSpacing.current.medium,
                            vertical = LocalSpacing.current.small
                        ),
                        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small)
                    ) {
                        items(count = repositories.itemCount) { index ->
                            repositories[index]?.let { repository ->
                                RepositoryItem(
                                    name = repository.name,
                                    description = repository.description,
                                    language = repository.language,
                                    onClick = {
                                        onRepositoryClick(repository.id)
                                    }
                                )
                            }
                        }
                    }

                    if (repositories.loadState.refresh is LoadState.Loading && !hasItems) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    if (repositories.loadState.refresh is LoadState.Error && !hasItems) {
                        ErrorState(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = LocalSpacing.current.large),
                            message = stringResource(
                                hopeapps.dedev.feature_repo.presentation.R.string.without_connection
                            )
                        )
                    }

                    if (repositories.loadState.refresh is LoadState.NotLoading && !hasItems) {
                        EmptyState(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = LocalSpacing.current.large),
                            message = stringResource(
                                hopeapps.dedev.feature_repo.presentation.R.string.nothing_here
                            ),
                            description = stringResource(
                                hopeapps.dedev.feature_repo.presentation.R.string.nothing_here_description
                            )
                        )
                    }
                }
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun UserScreenPreview() {

    val pagingItems = flowOf(
        PagingData.from(fakeData)
    ).collectAsLazyPagingItems()

    MaterialTheme {
        RepositoriesScreen(
            repositories = pagingItems,
            onSearchClick = {

            },
            onRepositoryClick = {

            },
            backButtonListener = { }
        )
    }
}

private val fakeData = listOf(
    Repository(
        id = 1,
        name = "",
        description = "",
        stars = 1,
        forks = 1,
        language = "",
        lastUpdate = "",
        isFork = false,
        watchers = 1,
        issues = 1,
        repoOwner = ""
    )
)