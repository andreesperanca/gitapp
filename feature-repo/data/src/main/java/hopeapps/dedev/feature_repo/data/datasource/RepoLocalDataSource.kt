package hopeapps.dedev.feature_repo.data.datasource

import hopeapps.dedev.common.Result
import hopeapps.dedev.core.database.model.RepoReadmeEntity
import hopeapps.dedev.core.database.model.RepoLanguageEntity
import hopeapps.dedev.core.database.model.RepositoryEntity

interface RepoLocalDataSource {

    suspend fun fetchRepositoryById(
        repoId: Long
    ): Result<RepositoryEntity>

    suspend fun saveRepoLanguages(
        repoLanguageEntity: RepoLanguageEntity
    )

    suspend fun saveRepoContent(repoContentEntity: RepoReadmeEntity)

    suspend fun fetchRepoLanguages(repoId: Long): Result<RepoLanguageEntity?>

    suspend fun fetchRepoContent(repoId: Long): Result<RepoReadmeEntity?>

}