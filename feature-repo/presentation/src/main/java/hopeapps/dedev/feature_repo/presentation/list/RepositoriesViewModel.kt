package hopeapps.dedev.feature_repo.presentation.list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.usecase.FetchRepositoryPaginatedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RepositoriesViewModel(
    private val searchRepositoryUseCase: FetchRepositoryPaginatedUseCase
) : ViewModel() {

    var repoPagingFlow = MutableStateFlow<PagingData<Repository>>(PagingData.empty())
        private set

    var userLogin: String = ""
        private set


    fun init(
        userLogin: String
    ) {
        this.userLogin = userLogin
        searchRepositories(userLogin)
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