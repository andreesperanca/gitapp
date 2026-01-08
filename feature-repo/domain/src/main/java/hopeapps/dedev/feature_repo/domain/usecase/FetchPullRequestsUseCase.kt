package hopeapps.dedev.feature_repo.domain.usecase

import hopeapps.dedev.common.Result
import hopeapps.dedev.feature_repo.domain.entity.RepoReadme
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository


class FetchPullRequestsUseCase(
    private val repoRepository: RepoRepository
) {
    suspend operator fun invoke(
        repoName: String,
        repoOwner: String,
        repoId: Long
    ): Result<RepoReadme> {
        return repoRepository.fetchRepositoryReadme(
            repoName = repoName,
            repoOwner = repoOwner,
            repoId = repoId
        )
    }
}