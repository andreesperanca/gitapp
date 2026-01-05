package hopeapps.dedev.feature_repo.presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import hopeapps.dedev.core.presentation.designsystem.components.DefaultTopAppBar
import hopeapps.dedev.core.presentation.designsystem.components.EmptyState
import hopeapps.dedev.core.presentation.designsystem.components.ErrorState
import hopeapps.dedev.core.presentation.designsystem.components.RepositoryItem
import hopeapps.dedev.feature_repo.domain.entity.RepoSort
import hopeapps.dedev.feature_repo.domain.entity.Repository
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchRepositoriesScreenRoot(
    userLogin: String, viewModel: RepoSearchViewModel = koinViewModel(), onBackListener: () -> Unit
) {
    val repositories = viewModel.repoPagingFlow.collectAsLazyPagingItems()

    LaunchedEffect(userLogin) {
        viewModel.init(userLogin)
    }

    SearchRepositoriesScreen(
        viewModel, repositories, onBackListener
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRepositoriesScreen(
    viewModel: RepoSearchViewModel,
    repositories: LazyPagingItems<Repository>,
    onBackListener: () -> Unit
) {

    var filterText by rememberSaveable { mutableStateOf("") }
    var isOnlyFork by rememberSaveable { mutableStateOf(false) }
    var isVisibleFilters by rememberSaveable { mutableStateOf(false) }
    var selected by rememberSaveable { mutableStateOf(RepoSort.Updated) }
    val listState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.filter_title), navigationIcon = {
                    IconButton(onClick = {
                        onBackListener()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                })
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(it)
        ) {

            DefaultSearchBar(
                modifier = Modifier.fillMaxWidth(),
                placeHolder = stringResource(R.string.search_repositories),
                onSearch = {

                },
                onQueryChange = { newQuery ->
                    filterText = newQuery
                    viewModel.updateLanguageFilter(filterText)
                },
                onSettingClick = {
                    isVisibleFilters = !isVisibleFilters
                })

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LocalSpacing.current.medium)
                    .align(Alignment.CenterHorizontally), onClick = {
                    viewModel.filterItems()
                }) {
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
                            checked = isOnlyFork, onCheckedChange = { isChecked ->
                                isOnlyFork = isChecked
                                viewModel.updateForkFilterType(isOnlyFork)
                            })
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
                            viewModel.updateSortFilter(selected)
                        },
                        selectedOption = selected,
                        labelMapper = { label ->
                            label.mapToLabel()
                        })
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
                    horizontal = LocalSpacing.current.medium, vertical = LocalSpacing.current.small
                ),
                title = stringResource(R.string.repositories),
                color = MaterialTheme.colorScheme.onBackground
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding(),
                contentPadding = PaddingValues(
                    horizontal = LocalSpacing.current.medium,
                    vertical = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small),
                state = listState
            ) {

                when (repositories.loadState.refresh) {
                    is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    is LoadState.Error -> {
                        item {
                            ErrorState(
                                modifier = Modifier.padding(LocalSpacing.current.large),
                                message = stringResource(R.string.error_message)
                            )
                        }
                    }

                    is LoadState.NotLoading -> {
                        if (repositories.itemCount == 0) {
                            item {
                                EmptyState(
                                    modifier = Modifier.padding(horizontal = LocalSpacing.current.large),
                                    message = stringResource(hopeapps.dedev.feature_repo.presentation.R.string.no_found_repositories),
                                    description = stringResource(hopeapps.dedev.feature_repo.presentation.R.string.try_modifier_filters)
                                )
                            }
                        } else {
                            items(
                                count = repositories.itemCount,
                                key = repositories.itemKey { it.id }
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


