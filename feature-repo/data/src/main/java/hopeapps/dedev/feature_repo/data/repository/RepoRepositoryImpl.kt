package hopeapps.dedev.feature_repo.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import hopeapps.dedev.common.Result
import hopeapps.dedev.core.database.AppDatabase
import hopeapps.dedev.core.network.GitApi
import hopeapps.dedev.feature_repo.data.datasource.RepoLocalDataSource
import hopeapps.dedev.feature_repo.data.datasource.RepoRemoteDataSource
import hopeapps.dedev.feature_repo.data.mapper.toDomain
import hopeapps.dedev.feature_repo.data.paging.IssuesRemoteMediator
import hopeapps.dedev.feature_repo.data.paging.PullRequestRemoteMediator
import hopeapps.dedev.feature_repo.data.paging.RepoRemoteMediator
import hopeapps.dedev.feature_repo.data.paging.SearchPagingSource
import hopeapps.dedev.feature_repo.domain.entity.Issue
import hopeapps.dedev.feature_repo.domain.entity.PullRequest
import hopeapps.dedev.feature_repo.domain.entity.RepoReadme
import hopeapps.dedev.feature_repo.domain.entity.RepoSearchFilter
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class RepoRepositoryImpl(
    private val gitApi: GitApi,
    private val db: AppDatabase,
    private val repoRemoteDataSource: RepoRemoteDataSource,
    private val repoLocalDataSource: RepoLocalDataSource
) : RepoRepository {

    override suspend fun fetchRepoById(repoId: Long): Result<Repository> {

        val response = repoLocalDataSource.fetchRepositoryById(repoId)

        return when (response) {
            is Result.Error -> {
                Result.Error(error = response.error)
            }

            is Result.Success -> {
                Result.Success(data = response.data.toDomain())
            }
        }
    }

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


    override suspend fun fetchRepositoryReadme(
        repoOwner: String,
        repoName: String
    ): Result<RepoReadme> {

        val response = repoRemoteDataSource.fetchRepositoryReadme(
            repoOwner = repoOwner,
            repoName = repoName
        )

        return when (response) {
            is Result.Error -> {
                Result.Error(error = response.error)
            }

            is Result.Success -> {
                Result.Success(data = response.data.toDomain())
            }
        }
    }

    override suspend fun fetchRepoLanguages(
        repoOwner: String,
        repoName: String
    ): Result<List<String>> {

        val response = repoRemoteDataSource.fetchRepositoryLanguages(
            repoOwner = repoOwner,
            repoName = repoName
        )

        return when (response) {
            is Result.Error -> {
                Result.Error(error = response.error)
            }

            is Result.Success -> {
                Result.Success(data = response.data.toDomain())
            }
        }
    }


    override fun fetchPullRequestsPaginated(
        repoName: String,
        repoOwner: String,
        repoId: Long
    ): Flow<PagingData<PullRequest>> {

        val pager = Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = PullRequestRemoteMediator(
                db = db,
                gitApi = gitApi,
                repoOwner = repoOwner,
                repoName = repoName,
                repoId = repoId
            ),
            pagingSourceFactory = {
                db.pullRequestDao().getPullRequests(repoId = repoId)
            }
        )

        return pager
            .flow
            .map { pagingData ->
                pagingData.map { pullRequestEntity ->
                    pullRequestEntity.toDomain()
                }
            }
    }

    override fun fetchIssuesPaginated(
        repoName: String,
        repoOwner: String,
        repoId: Long
    ): Flow<PagingData<Issue>> {

        val pager = Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = IssuesRemoteMediator(
                db = db,
                gitApi = gitApi,
                repoOwner = repoOwner,
                repoName = repoName,
                repoId = repoId
            ),
            pagingSourceFactory = {
                db.issueDao().getIssues(repoId = repoId)
            }
        )
        return pager
            .flow
            .map { pagingData ->
                pagingData.map { pullRequestEntity ->
                    pullRequestEntity.toDomain()
                }
            }
    }
}