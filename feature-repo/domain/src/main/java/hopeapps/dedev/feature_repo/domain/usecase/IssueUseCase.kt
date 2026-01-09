package hopeapps.dedev.feature_repo.domain.usecase

import androidx.paging.PagingData
import hopeapps.dedev.feature_repo.domain.entity.Issue
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow

class IssueUseCase(
    private val repoRepository: RepoRepository
) {
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