package hopeapps.dedev.feature_repo.presentation.search


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import hopeapps.dedev.feature_repo.domain.entity.ForkFilterType
import hopeapps.dedev.feature_repo.domain.entity.RepoSearchFilter
import hopeapps.dedev.feature_repo.domain.entity.RepoSort
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.usecase.SearchRepositoryPaginatedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RepoSearchViewModel(
    private val searchRepositoryPaginatedUseCase: SearchRepositoryPaginatedUseCase
) : ViewModel() {

    var repoPagingFlow = MutableStateFlow<PagingData<Repository>>(PagingData.empty())
        private set

    var repoSearchFilter = MutableStateFlow(RepoSearchFilter(user = "android"))
        private set

    fun onAction(action: RepoSearchAction) {
        when (action) {

            is RepoSearchAction.OnSearchClick -> {
                searchRepositories(
                    userFilterText = action.filterText,
                    filter = repoSearchFilter.value
                )
            }

            is RepoSearchAction.UpdateSortFilter -> {
                when (action.orderByFilter) {
                    "Estrelas" -> {
                        repoSearchFilter.update { repoSearchFilter ->
                            repoSearchFilter
                                .copy(sort = RepoSort.Stars)
                        }
                    }
                    "Forks" -> {
                        repoSearchFilter.update { repoSearchFilter ->
                            repoSearchFilter
                                .copy(sort = RepoSort.Forks)
                        }
                    }
                    "Última atualização" -> {
                        repoSearchFilter.update { repoSearchFilter ->
                            repoSearchFilter
                                .copy(sort = RepoSort.Updated)
                        }
                    }
                }
            }

            is RepoSearchAction.UpdatedForkFilterType -> {
                repoSearchFilter.update { repoSearchFilter ->
                    repoSearchFilter
                        .copy(forkFilter = if (action.isOnlyFork ) ForkFilterType.OnlyForks else ForkFilterType.All)
                }
            }

            is RepoSearchAction.UpdatedLanguageFilter -> {
                repoSearchFilter.update { repoSearchFilter ->
                    repoSearchFilter
                        .copy(
                            language = action.languageFilter
                        )
                }
            }
        }
    }

    init {
        searchRepositories("android", filter = RepoSearchFilter(user = "android"))
    }

    fun searchRepositories(userFilterText: String, filter: RepoSearchFilter) {
        viewModelScope.launch {
            searchRepositoryPaginatedUseCase(
                filter = filter,
                userFilterText = userFilterText
            ).cachedIn(viewModelScope)
                .collect { searchResponse ->
                    repoPagingFlow.value = searchResponse
                }
        }
    }
}