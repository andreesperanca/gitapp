package hopeapps.dedev.feature_repo.data.datasource

import hopeapps.dedev.common.Result
import hopeapps.dedev.core.database.model.RepositoryEntity
import hopeapps.dedev.core.network.models.RepoReadmeDto

interface RepoLocalDataSource {

    suspend fun fetchRepositoryById(
        repoId: Long
    ): Result<RepositoryEntity>

}