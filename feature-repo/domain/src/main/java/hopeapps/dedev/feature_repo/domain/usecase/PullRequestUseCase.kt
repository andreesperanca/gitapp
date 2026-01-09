package hopeapps.dedev.feature_repo.domain.usecase

import androidx.paging.PagingData
import hopeapps.dedev.feature_repo.domain.entity.PullRequest
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow

class PullRequestUseCase(
    private val repoRepository: RepoRepository
) {
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
}