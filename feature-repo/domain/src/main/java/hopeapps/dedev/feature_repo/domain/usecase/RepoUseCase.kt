package hopeapps.dedev.feature_repo.domain.usecase

import androidx.paging.PagingData
import hopeapps.dedev.common.Result
import hopeapps.dedev.feature_repo.domain.entity.RepoReadme
import hopeapps.dedev.feature_repo.domain.entity.RepoSearchFilter
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class RepoUseCase(
    private val repoRepository: RepoRepository
) {

    fun searchRepositoryPaginated(
        filter: RepoSearchFilter,
        userFilterText: String
    ): Flow<PagingData<Repository>> {
        return repoRepository.fetchSearchRepoPaginated(
            filter = filter,
            userFilterText = userFilterText
        )
    }

    fun fetchRepositoryPaginated(
        userFilterText: String
    ): Flow<PagingData<Repository>> {
        return repoRepository.fetchRepoPaginated(userFilterText)
    }


    suspend fun fetchRepoById(
        repoId: Long
    ): Result<Repository> {
        return withContext(Dispatchers.IO) {
            repoRepository.fetchRepoById(repoId = repoId)
        }
    }

    suspend fun fetchRepoLanguages(
        repoName: String,
        repoOwner: String,
        repoId: Long
    ): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            repoRepository.fetchRepoLanguages(
                repoName = repoName,
                repoOwner = repoOwner,
                repoId = repoId
            )
        }
    }

    suspend fun fetchRepositoryReadme(
        repoName: String,
        repoOwner: String,
        repoId: Long
    ): Result<RepoReadme> {
        return withContext(Dispatchers.IO) {
            repoRepository.fetchRepositoryReadme(
                repoName = repoName,
                repoOwner = repoOwner,
                repoId = repoId
            )
        }
    }
}