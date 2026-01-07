package hopeapps.dedev.feature_repo.presentation.details

import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import hopeapps.dedev.common.Result
import hopeapps.dedev.feature_repo.domain.entity.PullRequest
import hopeapps.dedev.feature_repo.domain.usecase.FetchRepoReadmeUseCase
import hopeapps.dedev.feature_repo.domain.usecase.RepoUseCase
import hopeapps.dedev.feature_repo.presentation.details.tab.RepoParams
import hopeapps.dedev.feature_repo.presentation.utils.IssueUiModel
import hopeapps.dedev.feature_repo.presentation.utils.toUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
class RepoDetailViewModel (
    val fetchRepoReadmeUseCase: FetchRepoReadmeUseCase,
    val repoUseCase: RepoUseCase
) : ViewModel() {

    var repoDetailState = MutableStateFlow(RepoDetailState())
        private set

    private val repoParams = MutableStateFlow<RepoParams?>(null)

    val pullRequestsPagingFlow: StateFlow<PagingData<PullRequest>> =
        repoParams
            .filterNotNull()
            .flatMapLatest { state ->
                repoUseCase.fetchPullRequestsPaginated(
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


    @RequiresApi(Build.VERSION_CODES.O)
    val issuesPagingFlow: StateFlow<PagingData<IssueUiModel>> =
        repoParams
            .filterNotNull()
            .flatMapLatest { state ->
                repoUseCase.fetchIssuesPaginated(
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
                            description = response.data.description
                        )
                    }
                    fetchRepoReadMe(
                        repoName = response.data.name,
                        repoOwner = response.data.repoOwner
                    )
                    fetchLanguageRepo(
                        repoName = response.data.name,
                        repoOwner = response.data.repoOwner
                    )
                }
                is Result.Error -> {

                }
            }
            delay(500)
            repoDetailState.update { state ->
                state.copy(isLoading = false)
            }
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
        repoOwner: String
    ) {
        val response = fetchRepoReadmeUseCase(
            repoName = repoName,
            repoOwner = repoOwner
        )

        when (response) {
            is Result.Error -> {
                //Error controller
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
        repoOwner: String
    ) {
        val response = repoUseCase.fetchRepoLanguages(
            repoName = repoName,
            repoOwner = repoOwner
        )

        when (response) {
            is Result.Error -> {
                //Error controller
            }
            is Result.Success -> {
                repoDetailState.update { state ->
                    state.copy(
                        languages = response.data
                    )
                }
            }
        }
    }
}