package hopeapps.dedev.feature_repo.data.datasource

import hopeapps.dedev.common.Result
import hopeapps.dedev.core.network.models.LanguagesDto
import hopeapps.dedev.core.network.models.RepoReadmeDto

interface RepoRemoteDataSource {
    suspend fun fetchRepositoryReadme(
        repoOwner: String,
        repoName: String
    ): Result<RepoReadmeDto>

    suspend fun fetchRepositoryLanguages(
        repoOwner: String,
        repoName: String
    ): Result<LanguagesDto>


}
