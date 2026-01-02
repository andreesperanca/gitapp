package hopeapps.dedev.feature_repo.domain.repository

import androidx.paging.PagingData
import hopeapps.dedev.feature_repo.domain.entity.RepoSearchFilter
import hopeapps.dedev.feature_repo.domain.entity.Repository
import kotlinx.coroutines.flow.Flow

interface RepoRepository {
    fun fetchRepoPaginated(userFilterText: String): Flow<PagingData<Repository>>
    fun fetchSearchRepoPaginated(filter: RepoSearchFilter, userFilterText: String): Flow<PagingData<Repository>>
}