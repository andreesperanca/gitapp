package hopeapps.dedev.feature_repo.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import hopeapps.dedev.core.database.AppDatabase
import hopeapps.dedev.core.network.GitApi
import hopeapps.dedev.feature_repo.data.paging.RepoRemoteMediator
import hopeapps.dedev.feature_repo.data.mapper.toDomain
import hopeapps.dedev.feature_repo.data.paging.SearchPagingSource
import hopeapps.dedev.feature_repo.domain.entity.RepoSearchFilter
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class RepoRepositoryImpl(
    private val gitApi: GitApi,
    private val db: AppDatabase
) : RepoRepository {

    override fun fetchRepoPaginated(userFilterText: String): Flow<PagingData<Repository>> {
        val pagingSourceFactory = {
            db.repoDao().getAllRepositories(userFilterText)
        }
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 30),
            remoteMediator = RepoRemoteMediator(
                db = db,
                gitApi = gitApi,
                useFilterText = userFilterText
            ),
            pagingSourceFactory = pagingSourceFactory
        )

        return pager
            .flow
            .map { pagingData ->
                pagingData.map { repositoryEntity ->
                    repositoryEntity.toDomain()
                }
            }
    }

    override fun fetchSearchRepoPaginated(
        filter: RepoSearchFilter,
        userFilterText: String
    ): Flow<PagingData<Repository>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 30),
            pagingSourceFactory = {
                SearchPagingSource(
                    gitApi = gitApi,
                    filter = filter
                )
            }
        ).flow
            .map { pagingData ->
                pagingData.map { repositoryEntity ->
                    repositoryEntity.toDomain()
                }
            }
    }
}