package hopeapps.dedev.feature_repo.presentation.list


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import hopeapps.dedev.core.presentation.designsystem.LocalSpacing
import hopeapps.dedev.core.presentation.designsystem.R
import hopeapps.dedev.core.presentation.designsystem.components.DefaultTopAppBar
import hopeapps.dedev.core.presentation.designsystem.components.RepositoryItem
import hopeapps.dedev.feature_repo.domain.entity.Repository
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel


@Composable
fun RepositoriesScreenRoot(
    viewModel: RepositoriesViewModel = koinViewModel(),
    userLogin: String,
    navigateToSearchRepositories: (userLogin: String) -> Unit,
    navigateToRepositoryDetails: (repository: Repository) -> Unit,
    backButtonListener: () -> Unit
) {
    viewModel.init(userLogin)
    val repositories = viewModel.repoPagingFlow.collectAsLazyPagingItems()

    RepositoriesScreen(
        repositories = repositories,
        onSearchClick = {
            navigateToSearchRepositories(userLogin)
        },
        onRepositoryClick = { repository ->
            navigateToRepositoryDetails(repository)
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
    onRepositoryClick: (repository: Repository) -> Unit,
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(LocalSpacing.current.small),
                verticalArrangement = Arrangement
                    .spacedBy(LocalSpacing.current.extraSmall)
            ) {
                items(
                    count = repositories.itemCount,
                    key = repositories.itemKey { repository -> repository.id }
                ) { index ->
                    val repository = repositories[index]
                    if (repository != null) {
                        RepositoryItem(
                            name = repository.name,
                            description = repository.description,
                            language = repository.language,
                            onClick = {
                                onRepositoryClick(repository)
                            }
                        )
                    }
                }
            }
        }
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
        isFork = false
    )
)