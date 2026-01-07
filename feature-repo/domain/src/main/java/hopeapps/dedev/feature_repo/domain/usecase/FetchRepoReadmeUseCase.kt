package hopeapps.dedev.feature_repo.domain.usecase

import hopeapps.dedev.common.Result
import hopeapps.dedev.feature_repo.domain.entity.RepoReadme
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class FetchRepoReadmeUseCase(
    private val repoRepository: RepoRepository
) {
    suspend operator fun invoke(
        repoName: String,
        repoOwner: String
    ): Result<RepoReadme> {
        return withContext(Dispatchers.IO) {
            repoRepository.fetchRepositoryReadme(
                repoName = repoName,
                repoOwner = repoOwner
            )
        }
    }
}