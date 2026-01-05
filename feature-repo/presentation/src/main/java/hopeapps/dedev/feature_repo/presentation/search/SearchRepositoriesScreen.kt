package hopeapps.dedev.feature_repo.presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import hopeapps.dedev.core.presentation.designsystem.LocalSpacing
import hopeapps.dedev.core.presentation.designsystem.R
import hopeapps.dedev.core.presentation.designsystem.components.ChipSelector
import hopeapps.dedev.core.presentation.designsystem.components.DefaultSearchBar
import hopeapps.dedev.core.presentation.designsystem.components.DefaultText
import hopeapps.dedev.core.presentation.designsystem.components.DefaultTitle
import hopeapps.dedev.core.presentation.designsystem.components.EmptyState
import hopeapps.dedev.core.presentation.designsystem.components.ErrorState
import hopeapps.dedev.core.presentation.designsystem.components.RepositoryItem
import hopeapps.dedev.feature_repo.domain.entity.RepoSort
import hopeapps.dedev.feature_repo.domain.entity.Repository
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchRepositoriesScreenRoot(
    userLogin: String,
    viewModel: RepoSearchViewModel = koinViewModel()
) {
    val repositories = viewModel.repoPagingFlow.collectAsLazyPagingItems()
    viewModel.init(userLogin)

    SearchRepositoriesScreen(
        onAction = viewModel::onAction,
        repositories
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRepositoriesScreen(
    onAction: (RepoSearchAction) -> Unit,
    repositories: LazyPagingItems<Repository>
) {

    var filterText by rememberSaveable { mutableStateOf("") }
    var isOnlyFork by rememberSaveable { mutableStateOf(false) }
    var isVisibleFilters by rememberSaveable { mutableStateOf(false) }
    var selected by rememberSaveable { mutableStateOf(RepoSort.Updated) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Voltar e título com o nome do usuário
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(it)
        ) {

            DefaultSearchBar(
                modifier = Modifier
                    .fillMaxWidth(),
                placeHolder = stringResource(R.string.search_repositories),
                onSearch = {

                },
                onQueryChange = { newQuery ->
                    filterText = newQuery
                    onAction(RepoSearchAction.UpdatedLanguageFilter(languageFilter = newQuery))
                },
                onSettingClick = {
                    isVisibleFilters = !isVisibleFilters
                }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LocalSpacing.current.medium)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    onAction(RepoSearchAction.OnSearchClick(filterText))
                }
            ) {
                Text(text = stringResource(R.string.search_title))
            }

            AnimatedVisibility(isVisibleFilters) {
                Card(
                    modifier = Modifier.padding(
                        horizontal = LocalSpacing.current.small,
                        vertical = LocalSpacing.current.small
                    ),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {

                    Row(
                        modifier = Modifier.padding(horizontal = LocalSpacing.current.medium),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DefaultText(
                            modifier = Modifier.weight(1f),
                            text = stringResource(hopeapps.dedev.feature_repo.presentation.R.string.only_forks)
                        )

                        Switch(
                            checked = isOnlyFork,
                            onCheckedChange = { isChecked ->
                                isOnlyFork = isChecked
                                onAction(RepoSearchAction.UpdatedForkFilterType(isOnlyFork = isChecked))
                            }
                        )
                    }

                    DefaultText(
                        modifier = Modifier.padding(horizontal = LocalSpacing.current.medium),
                        text = stringResource(hopeapps.dedev.feature_repo.presentation.R.string.order_by)
                    )

                    ChipSelector(
                        modifier = Modifier.padding(horizontal = LocalSpacing.current.medium),
                        options = RepoSort.entries,
                        onOptionSelected = { optionSelected ->
                            selected = optionSelected
                            onAction(RepoSearchAction.UpdateSortFilter(orderByFilter = optionSelected))
                        },
                        selectedOption = selected,
                        labelMapper = { label ->
                            label.mapToLabel()
                        }
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.medium,
                        vertical = LocalSpacing.current.medium
                    )
                    .fillMaxWidth()
            )

            DefaultTitle(
                modifier = Modifier.padding(
                    horizontal = LocalSpacing.current.medium,
                    vertical = LocalSpacing.current.small
                ),
                title = stringResource(R.string.repositories),
                color = MaterialTheme.colorScheme.onBackground
            )


            when (val state = repositories.loadState.refresh) {

                is LoadState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.Error -> {
                    ErrorState(
                        modifier = Modifier.padding(horizontal = LocalSpacing.current.large),
                        message = state.error.message ?: stringResource(hopeapps.dedev.feature_repo.presentation.R.string.error_message)
                    )
                }

                is LoadState.NotLoading -> {
                    if (repositories.itemCount == 0) {
                        EmptyState(
                            modifier = Modifier.padding(horizontal = LocalSpacing.current.large),
                            message = stringResource(hopeapps.dedev.feature_repo.presentation.R.string.no_found_repositories),
                            description = stringResource(hopeapps.dedev.feature_repo.presentation.R.string.try_modifier_filters)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = LocalSpacing.current.medium),
                            verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small)
                        ) {
                            items(
                                count = repositories.itemCount,
                                key = repositories.itemKey { item -> item.id }
                            ) { index ->
                                repositories[index]?.let { repository ->
                                    RepositoryItem(
                                        name = repository.name,
                                        description = repository.description,
                                        language = repository.language
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


