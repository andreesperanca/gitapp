package hopeapps.dedev.feature_repo.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import hopeapps.dedev.core.database.AppDatabase
import hopeapps.dedev.core.database.model.PullRequestEntity
import hopeapps.dedev.core.database.model.PullRequestRemoteKeysEntity
import hopeapps.dedev.core.network.GitApi
import hopeapps.dedev.feature_repo.data.mapper.toEntity

@OptIn(ExperimentalPagingApi::class)
class PullRequestRemoteMediator(
    private val db : AppDatabase,
    private val gitApi: GitApi,
    private val repoOwner: String,
    private val repoName: String,
    private val repoId: Long
): RemoteMediator<Int, PullRequestEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PullRequestEntity>
    ): MediatorResult {
        return try {
            val currentPage = when(loadType) {

                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKey = db.pullRequestRemoteKeyDao().getLastRemoteKey() ?: return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKey.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val remoteKeys = db.pullRequestRemoteKeyDao().getLastRemoteKey()
                    val nextPage = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val pullRequests = gitApi.fetchRepoPullRequests(
                owner = repoOwner,
                repo = repoName,
                page = currentPage,
                perPage = state.config.pageSize
            )

            val endOfPaginationReached = pullRequests.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.pullRequestRemoteKeyDao().clearRemoteKeys()
                    db.pullRequestDao().clearAll(repoId = repoId)
                }

                val keys = pullRequests.map { pr ->
                    PullRequestRemoteKeysEntity(
                        pullRequestId = pr.id,
                        prevKey = prevPage,
                        nextKey = nextPage
                    )
                }

                db.pullRequestRemoteKeyDao().insertAll(keys)
                db.pullRequestDao().upsertAll(prs = pullRequests.map { pr -> pr.toEntity(repoId = repoId) })
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch(e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PullRequestEntity>
    ): PullRequestRemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.repoId?.let { id ->
                db.pullRequestRemoteKeyDao().remoteKeysByPullRequestId(prId = id)
            }
        }
    }

}