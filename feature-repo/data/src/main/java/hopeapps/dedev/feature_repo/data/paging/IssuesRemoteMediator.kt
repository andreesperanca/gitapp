package hopeapps.dedev.feature_repo.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import hopeapps.dedev.core.database.AppDatabase
import hopeapps.dedev.core.database.dao.IssueDao
import hopeapps.dedev.core.database.dao.IssueRemoteKeyDao
import hopeapps.dedev.core.database.model.IssueEntity
import hopeapps.dedev.core.database.model.IssueRemoteKeysEntity
import hopeapps.dedev.core.database.model.PullRequestEntity
import hopeapps.dedev.core.database.model.PullRequestRemoteKeysEntity
import hopeapps.dedev.core.network.GitApi
import hopeapps.dedev.feature_repo.data.mapper.toEntity

@OptIn(ExperimentalPagingApi::class)
class IssuesRemoteMediator(
    private val gitApi: GitApi,
    private val repoOwner: String,
    private val repoName: String,
    private val repoId: Long,
    private val db: AppDatabase
): RemoteMediator<Int, IssueEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, IssueEntity>
    ): MediatorResult {
        return try {
            val currentPage = when(loadType) {

                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKey = db.issueRemoteKeyDao().getLastRemoteKey() ?: return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKey.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val remoteKeys = db.issueRemoteKeyDao().getLastRemoteKey()
                    val nextPage = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val issues = gitApi.fetchRepoIssues(
                owner = repoOwner,
                repo = repoName,
                page = currentPage,
                perPage = state.config.pageSize
            )

            val endOfPaginationReached = issues.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.issueRemoteKeyDao().clearRemoteKeys()
                    db.issueDao().clearAll(repoId = repoId)
                }

                val keys = issues.map { issue ->
                    IssueRemoteKeysEntity(
                        issueId = issue.id,
                        prevKey = prevPage,
                        nextKey = nextPage
                    )
                }

                db.issueRemoteKeyDao().insertAll(remoteKey = keys)
                db.issueDao().upsertAll(issues = issues.map { issue -> issue.toEntity(repoId = repoId) })
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch(e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, IssueEntity>
    ): IssueRemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.repoId?.let { id ->
                db.issueRemoteKeyDao().remoteKeysByIssuesId(issueId = id)
            }
        }
    }

}