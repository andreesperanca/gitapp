package hopeapps.dedev.feature_repo.domain.usecase

import androidx.paging.PagingData
import hopeapps.dedev.feature_repo.domain.entity.RepoSearchFilter
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.entity.SearchResponse
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow


class SearchRepositoryPaginatedUseCase(
    private val repoRepository: RepoRepository
) {
    operator fun invoke(
        filter: RepoSearchFilter,
        userFilterText: String
    ): Flow<PagingData<Repository>> {
        return repoRepository.fetchSearchRepoPaginated(
            filter = filter,
            userFilterText = userFilterText
        )
    }
}