package hopeapps.dedev.feature_repo.domain.repository

import androidx.paging.PagingData
import hopeapps.dedev.common.Result
import hopeapps.dedev.feature_repo.domain.entity.Issue
import hopeapps.dedev.feature_repo.domain.entity.PullRequest
import hopeapps.dedev.feature_repo.domain.entity.RepoReadme
import hopeapps.dedev.feature_repo.domain.entity.RepoSearchFilter
import hopeapps.dedev.feature_repo.domain.entity.Repository
import kotlinx.coroutines.flow.Flow

interface RepoRepository {
    fun fetchRepoPaginated(userFilterText: String): Flow<PagingData<Repository>>
    fun fetchSearchRepoPaginated(filter: RepoSearchFilter, userFilterText: String): Flow<PagingData<Repository>>

    fun fetchPullRequestsPaginated(repoName: String, repoOwner: String, repoId: Long): Flow<PagingData<PullRequest>>
    fun fetchIssuesPaginated(repoName: String, repoOwner: String, repoId: Long): Flow<PagingData<Issue>>


    suspend fun fetchRepositoryReadme(repoOwner: String, repoName: String, repoId: Long): Result<RepoReadme>
    suspend fun fetchRepoById(repoId: Long): Result<Repository>
    suspend fun fetchRepoLanguages(repoOwner: String, repoName: String, repoId: Long): Result<List<String>>
}