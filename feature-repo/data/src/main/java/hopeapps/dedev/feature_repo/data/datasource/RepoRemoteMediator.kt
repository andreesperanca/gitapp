package hopeapps.dedev.feature_repo.data.datasource

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import hopeapps.dedev.core.database.AppDatabase
import hopeapps.dedev.core.database.model.RepoItemRemoteKeysEntity
import hopeapps.dedev.core.database.model.RepositoryEntity
import hopeapps.dedev.core.network.GitApi
import hopeapps.dedev.feature_repo.data.mapper.toRepositoryEntity


@OptIn(ExperimentalPagingApi::class)
class RepoRemoteMediator(
    private val db : AppDatabase,
    private val gitApi: GitApi,
    private val useFilterText: String
): RemoteMediator<Int, RepositoryEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepositoryEntity>
    ): MediatorResult {
        return try {
            val currentPage = when(loadType) {

                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKey = db.remoteKeyDao().getLastRemoteKey() ?: return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKey.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val remoteKeys = db.remoteKeyDao().getLastRemoteKey()
                    val nextPage = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val repositories = gitApi.fetchRemoteRepositories(
                userFilterText = useFilterText,
                page = currentPage,
                perPage = state.config.pageSize
            )

            val endOfPaginationReached = repositories.isEmpty()
            Log.d("log_debug", "Chamada API currentPage = $currentPage e perPage ${state.config.pageSize} items carregados: ${repositories.size}")

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeyDao().clearRemoteKeys()
                    db.repoDao().clearAll()
                }

                val keys = repositories.map { repositoryDto ->
                    RepoItemRemoteKeysEntity(
                        repoId = repositoryDto.id,
                        prevKey = prevPage,
                        nextKey = nextPage
                    )
                }

                db.remoteKeyDao().insertAll(keys)
                db.repoDao().upsertAll(repositories.map { it.toRepositoryEntity() })
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch(e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, RepositoryEntity>
    ): RepoItemRemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                db.remoteKeyDao().remoteKeysByRepoId(repoId = id)
            }
        }
    }

    private suspend fun getLastRemoteKey(
        state: PagingState<Int, RepositoryEntity>
    ): RepoItemRemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let {
                db.remoteKeyDao().remoteKeysByRepoId(repoId = it.id)
            }
    }
}