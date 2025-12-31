package hopeapps.dedev.feature_repo.presentation.list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.usecase.FetchRepositoryPaginatedUseCase
import hopeapps.dedev.feature_repo.presentation.search.RepoSearchAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RepositoriesViewModel(
    private val searchRepositoryUseCase: FetchRepositoryPaginatedUseCase
) : ViewModel() {

    var state = MutableStateFlow(RepositoriesState())
        private set

    var event = MutableSharedFlow<RepositoriesEvent>()
        private set

    var repoPagingFlow = MutableStateFlow<PagingData<Repository>>(PagingData.empty())
        private set


    init {
        searchRepositories("android")
    }


    fun onAction(action: RepoSearchAction) {

    }

    fun searchRepositories(userFilterText: String) {
        viewModelScope.launch {
            searchRepositoryUseCase(userFilterText)
                .cachedIn(viewModelScope)
                .collect {
                    repoPagingFlow.value = it
                }
        }
    }
}