package hopeapps.dedev.feature_repo.presentation.details

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import hopeapps.dedev.common.GitException
import hopeapps.dedev.common.Result
import hopeapps.dedev.feature_repo.domain.entity.PullRequest
import hopeapps.dedev.feature_repo.domain.usecase.IssueUseCase
import hopeapps.dedev.feature_repo.domain.usecase.PullRequestUseCase
import hopeapps.dedev.feature_repo.domain.usecase.RepoUseCase
import hopeapps.dedev.feature_repo.presentation.details.DetailEvent.BackListener
import hopeapps.dedev.feature_repo.presentation.details.DetailEvent.OpenRepoInWeb
import hopeapps.dedev.feature_repo.presentation.details.DetailEvent.ShareRepo
import hopeapps.dedev.feature_repo.presentation.details.tab.RepoParams
import hopeapps.dedev.feature_repo.presentation.mapper.toUiModel
import hopeapps.dedev.feature_repo.presentation.model.IssueUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class)
class RepoDetailViewModel(
    val repoUseCase: RepoUseCase,
    val pullRequestUseCase: PullRequestUseCase,
    val issueUseCase: IssueUseCase
) : ViewModel() {

    var repoDetailState = MutableStateFlow(RepoDetailState())
        private set

    var event = MutableSharedFlow<DetailEvent>()
        private set

    private val repoParams = MutableStateFlow<RepoParams?>(null)

    val pullRequestsPagingFlow: StateFlow<PagingData<PullRequest>> =
        repoParams
            .filterNotNull()
            .flatMapLatest { state ->
                pullRequestUseCase.fetchPullRequestsPaginated(
                    repoName = state.repoName,
                    repoOwner = state.repoOwner,
                    repoId = state.repoId
                )
            }
            .cachedIn(viewModelScope)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PagingData.empty()
            )


    val issuesPagingFlow: StateFlow<PagingData<IssueUiModel>> =
        repoParams
            .filterNotNull()
            .flatMapLatest { state ->
                issueUseCase.fetchIssuesPaginated(
                    repoName = state.repoName,
                    repoOwner = state.repoOwner,
                    repoId = state.repoId
                ).map { pagingData ->
                    pagingData.map { issue ->
                        issue.toUiModel()
                    }
                }
            }
            .cachedIn(viewModelScope)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PagingData.empty()
            )

    fun onAction(action: DetailAction) {
        when (action) {
            is DetailAction.OpenRepoInWeb -> {
                sendEvent(OpenRepoInWeb(repoDetailState.value.urlRepo))
            }

            is DetailAction.ShareRepo -> {
                sendEvent(ShareRepo(repoDetailState.value.urlRepo))
            }

            DetailAction.BackListener -> {
                sendEvent(BackListener)
            }
        }
    }

    fun start(
        repoId: Long
    ) {
        viewModelScope.launch {

            repoDetailState.update { state -> state.copy(isLoading = true) }
            val response = repoUseCase.fetchRepoById(repoId)

            when (response) {
                is Result.Success -> {
                    repoParams.update { repoParam ->
                        RepoParams(
                            repoName = response.data.name,
                            repoOwner = response.data.repoOwner,
                            repoId = repoId
                        )
                    }
                    repoDetailState.update { state ->
                        state.copy(
                            stars = response.data.stars,
                            forks = response.data.forks,
                            watchers = response.data.watchers,
                            issues = response.data.issues,
                            name = response.data.name,
                            description = response.data.description,
                            urlRepo = buildUrl(
                                repoName = response.data.name,
                                repoOwner = response.data.repoOwner
                            )
                        )
                    }
                    fetchRepoReadMe(
                        repoName = response.data.name,
                        repoOwner = response.data.repoOwner,
                        repoId = repoId
                    )
                    fetchLanguageRepo(
                        repoName = response.data.name,
                        repoOwner = response.data.repoOwner,
                        repoId = repoId
                    )
                }

                is Result.Error -> {
                    handleError(exception = response.error)
                }
            }
            repoDetailState.update { state ->
                state.copy(isLoading = false)
            }
        }
    }


    private fun sendEvent(newEvent: DetailEvent) {
        viewModelScope.launch {
            event.emit(newEvent)
        }
    }


    private fun decodeReadme(base64Content: String): String {
        val cleanBase64 = base64Content
            .replace("\n", "")
            .replace("\r", "")
            .trim()

        return String(
            Base64.decode(cleanBase64, Base64.DEFAULT),
            Charsets.UTF_8
        )
    }

    private suspend fun fetchRepoReadMe(
        repoName: String,
        repoOwner: String,
        repoId: Long
    ) {
        val response = repoUseCase.fetchRepositoryReadme(
            repoName = repoName,
            repoOwner = repoOwner,
            repoId = repoId
        )

        when (response) {
            is Result.Error -> {
                handleError(exception = response.error)
            }

            is Result.Success -> {
                repoDetailState.update { state ->
                    state.copy(
                        readme = decodeReadme(response.data.content)
                    )
                }
            }
        }
    }

    private suspend fun fetchLanguageRepo(
        repoName: String,
        repoOwner: String,
        repoId: Long
    ) {
        val response = repoUseCase.fetchRepoLanguages(
            repoName = repoName,
            repoOwner = repoOwner,
            repoId = repoId
        )

        when (response) {
            is Result.Error -> {
                handleError(exception = response.error)
            }

            is Result.Success -> {
                repoDetailState.update { state ->
                    state.copy(
                        languages = response.data.joinToString(" ,")
                    )
                }
            }
        }
    }

    private fun buildUrl(repoName: String, repoOwner: String): String {
        return "https://github.com/$repoOwner/$repoName"
    }

    private fun handleError(exception: GitException) {
        when (exception) {
            GitException.NetworkError -> {
                sendEvent(DetailEvent.ShowSnackBar(message = "Network Error"))
            }

            GitException.UnknownError -> {
                sendEvent(DetailEvent.ShowSnackBar(message = "Unknown Error"))
            }
        }
    }
}