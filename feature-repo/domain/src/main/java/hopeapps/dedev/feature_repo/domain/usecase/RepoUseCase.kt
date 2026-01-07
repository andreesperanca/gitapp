package hopeapps.dedev.feature_repo.domain.usecase

import androidx.paging.PagingData
import hopeapps.dedev.common.Result
import hopeapps.dedev.feature_repo.domain.entity.Issue
import hopeapps.dedev.feature_repo.domain.entity.PullRequest
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class RepoUseCase(
    private val repoRepository: RepoRepository
) {


    suspend fun fetchRepoById(
        repoId: Long
    ): Result<Repository> {
        return withContext(Dispatchers.IO) {
            repoRepository.fetchRepoById(repoId = repoId)
        }
    }

    suspend fun fetchRepoLanguages(
        repoName: String,
        repoOwner: String
    ): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            repoRepository.fetchRepoLanguages(
                repoName = repoName,
                repoOwner = repoOwner
            )
        }
    }



    fun fetchPullRequestsPaginated(
        repoName: String,
        repoOwner: String,
        repoId: Long
    ): Flow<PagingData<PullRequest>> {
        return repoRepository.fetchPullRequestsPaginated(
            repoName = repoName,
            repoOwner = repoOwner,
            repoId = repoId
        )
    }



    fun fetchIssuesPaginated(
        repoName: String,
        repoOwner: String,
        repoId: Long
    ): Flow<PagingData<Issue>> {
        return repoRepository.fetchIssuesPaginated(
            repoName = repoName,
            repoOwner = repoOwner,
            repoId = repoId
        )
    }

}