package hopeapps.dedev.feature_repo.data.datasource

import hopeapps.dedev.common.Result
import hopeapps.dedev.common.safeApiCall
import hopeapps.dedev.core.network.GitApi
import hopeapps.dedev.core.network.models.LanguagesDto
import hopeapps.dedev.core.network.models.RepoReadmeDto

class RepoRemoteDataSourceImpl(
    private val gitApi: GitApi
): RepoRemoteDataSource {


    override suspend fun fetchRepositoryReadme(
        repoOwner: String,
        repoName: String
    ): Result<RepoReadmeDto> {
        return safeApiCall {
            gitApi.fetchReadmeRepository(owner = repoOwner, repo = repoName)
        }
    }

    override suspend fun fetchRepositoryLanguages(
        repoOwner: String,
        repoName: String
    ): Result<LanguagesDto> {
        return safeApiCall {
            gitApi.getRepositoryLanguages(owner = repoOwner, repo = repoName)
        }
    }

}