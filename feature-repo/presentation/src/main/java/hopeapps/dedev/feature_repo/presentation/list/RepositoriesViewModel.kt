package hopeapps.dedev.feature_repo.presentation.list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.usecase.RepoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn


@OptIn(ExperimentalCoroutinesApi::class)
class RepositoriesViewModel(
    private val repoUseCase: RepoUseCase
) : ViewModel() {

    var userTrigger = MutableStateFlow<String?>(null)
        private set

    val repoPagingFlow: StateFlow<PagingData<Repository>> =
        userTrigger
            .filterNotNull()
            .flatMapLatest { userLogin ->
                repoUseCase.fetchRepositoryPaginated(userTrigger.value ?: "andreesperanca")
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