package hopeapps.dedev.feature_repo.presentation.list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.usecase.FetchRepositoryPaginatedUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class)
class RepositoriesViewModel(
    private val searchRepositoryUseCase: FetchRepositoryPaginatedUseCase
) : ViewModel() {

    var userLogin: String = ""
        private set

    private val userTrigger = MutableStateFlow<String?>(null)

    val repoPagingFlow: StateFlow<PagingData<Repository>> =
        userTrigger
            .filterNotNull()
            .flatMapLatest { userLogin ->
                searchRepositoryUseCase(userTrigger.value ?: "andreesperanca")
            }
            .cachedIn(viewModelScope)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PagingData.empty()
            )

    fun init(userLogin: String) {
        userTrigger.value = userLogin
    }
}