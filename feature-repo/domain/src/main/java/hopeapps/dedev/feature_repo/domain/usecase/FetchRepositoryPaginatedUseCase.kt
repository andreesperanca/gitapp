package hopeapps.dedev.feature_repo.domain.usecase

import androidx.paging.PagingData
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow


class FetchRepositoryPaginatedUseCase(
    private val repoRepository: RepoRepository
) {
    operator fun invoke(
        userFilterText: String
    ): Flow<PagingData<Repository>> {
        return repoRepository.fetchRepoPaginated(userFilterText)
    }
}