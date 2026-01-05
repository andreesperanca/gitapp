package hopeapps.dedev.feature_repo.presentation.search


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import hopeapps.dedev.feature_repo.domain.entity.ForkFilterType
import hopeapps.dedev.feature_repo.domain.entity.RepoSearchFilter
import hopeapps.dedev.feature_repo.domain.entity.RepoSort
import hopeapps.dedev.feature_repo.domain.entity.RepoSort.Forks
import hopeapps.dedev.feature_repo.domain.entity.RepoSort.Stars
import hopeapps.dedev.feature_repo.domain.entity.RepoSort.Updated
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.usecase.SearchRepositoryPaginatedUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


@OptIn(ExperimentalCoroutinesApi::class)
class RepoSearchViewModel(
    private val searchRepositoryPaginatedUseCase: SearchRepositoryPaginatedUseCase
) : ViewModel() {

    var repoSearchFilter = MutableStateFlow(RepoSearchFilter())
        private set

    private val searchTrigger = MutableStateFlow(RepoSearchFilter())

    val repoPagingFlow: StateFlow<PagingData<Repository>> =
        searchTrigger.flatMapLatest { filter ->
            searchRepositoryPaginatedUseCase(filter = filter, userFilterText = filter.user ?: "")
        }.cachedIn(viewModelScope)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PagingData.empty()
            )

    fun init(userLogin: String) {
        repoSearchFilter.update { repoSearchFilter ->
            repoSearchFilter.copy(user = userLogin)
        }
        searchTrigger.update { repoSearchFilter.value }
    }

    fun onAction(action: RepoSearchAction) {
        when (action) {

            is RepoSearchAction.OnSearchClick -> {
                searchTrigger.update { repoSearchFilter.value }
            }

            is RepoSearchAction.UpdateSortFilter -> {
                when (action.orderByFilter) {
                    Stars -> {
                        repoSearchFilter.update { repoSearchFilter ->
                            repoSearchFilter.copy(sort = RepoSort.Stars)
                        }
                    }

                    Forks -> {
                        repoSearchFilter.update { repoSearchFilter ->
                            repoSearchFilter.copy(sort = RepoSort.Forks)
                        }
                    }

                    Updated -> {
                        repoSearchFilter.update { repoSearchFilter ->
                            repoSearchFilter.copy(sort = RepoSort.Updated)
                        }
                    }
                }
            }

            is RepoSearchAction.UpdatedForkFilterType -> {
                repoSearchFilter.update { repoSearchFilter ->
                    repoSearchFilter.copy(forkFilter = if (action.isOnlyFork) ForkFilterType.OnlyForks else ForkFilterType.All)
                }
            }

            is RepoSearchAction.UpdatedLanguageFilter -> {
                repoSearchFilter.update { repoSearchFilter ->
                    repoSearchFilter.copy(
                        language = action.languageFilter
                    )
                }
            }

            is RepoSearchAction.OnRepositorySelected -> {

            }
        }
    }
}