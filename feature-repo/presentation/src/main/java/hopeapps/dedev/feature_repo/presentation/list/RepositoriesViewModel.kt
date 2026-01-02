package hopeapps.dedev.feature_repo.presentation.list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.usecase.FetchRepositoryPaginatedUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class RepositoriesViewModel(
    private val searchRepositoryUseCase: FetchRepositoryPaginatedUseCase
) : ViewModel() {

    var repoPagingFlow = flowOf<PagingData<Repository>>(PagingData.empty())
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
        repoPagingFlow = searchRepositoryUseCase
            .invoke(userFilterText)
            .cachedIn(viewModelScope)
    }
}