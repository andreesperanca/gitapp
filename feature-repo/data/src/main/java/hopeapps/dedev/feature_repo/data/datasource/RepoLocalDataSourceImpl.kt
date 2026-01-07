package hopeapps.dedev.feature_repo.data.datasource

import hopeapps.dedev.common.GitException
import hopeapps.dedev.common.Result
import hopeapps.dedev.core.database.dao.RepoDao
import hopeapps.dedev.core.database.model.RepositoryEntity

class RepoLocalDataSourceImpl(
    private val repoDao: RepoDao
) : RepoLocalDataSource {

    override suspend fun fetchRepositoryById(repoId: Long): Result<RepositoryEntity> {
        try {
            val repository = repoDao.getRepositoryById(repoId = repoId)
            return Result.Success(data = repository)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(GitException.UnknownError)
        }
    }

}