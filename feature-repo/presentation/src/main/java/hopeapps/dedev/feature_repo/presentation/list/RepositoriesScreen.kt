package hopeapps.dedev.feature_repo.presentation.list


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import hopeapps.dedev.core.presentation.designsystem.components.DefaultTitle
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.presentation.search.RepoSearchAction
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel


@Composable
fun RepositoriesScreenRoot(
    viewModel: RepositoriesViewModel = koinViewModel(),
    userLogin: String
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val repositories = viewModel.repoPagingFlow.collectAsLazyPagingItems()

    RepositoryListScreen(
        state = state,
        repositories = repositories,
        onAction = viewModel::onAction
    )
}


@Composable
fun RepositoryListScreen(
    state: RepositoriesState,
    repositories: LazyPagingItems<Repository>,
    onAction: (RepoSearchAction) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        DefaultTitle(
            modifier = Modifier,
            title = "Repositories",
            color = MaterialTheme.colorScheme.onBackground
        )

        LazyColumn {
            items(
                count = repositories.itemCount,
                key = repositories.itemKey { repository -> repository.id }
            ) { index ->

                val repository = repositories[index]
                if (repository != null) {
                    Text(
                        modifier = Modifier.height(200.dp),
                        text = repository.name
                    )
                }
            }
        }
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


@Composable
@Preview(showBackground = true)
fun UserScreenPreview() {

    val pagingItems = flowOf(
        PagingData.from(fakeData)
    ).collectAsLazyPagingItems()

    MaterialTheme {
        RepositoryListScreen(
            state = RepositoriesState(),
            onAction = {
            },
            repositories = pagingItems
        )
    }
}