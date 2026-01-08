package hopeapps.dedev.feature_repo.data.datasource

import hopeapps.dedev.common.GitException
import hopeapps.dedev.common.Result
import hopeapps.dedev.core.database.dao.RepoReadmeDao
import hopeapps.dedev.core.database.dao.RepoDao
import hopeapps.dedev.core.database.dao.RepoLanguageDao
import hopeapps.dedev.core.database.model.RepoReadmeEntity
import hopeapps.dedev.core.database.model.RepoLanguageEntity
import hopeapps.dedev.core.database.model.RepositoryEntity

class RepoLocalDataSourceImpl(
    private val repoDao: RepoDao,
    private val repoLanguageDao: RepoLanguageDao,
    private val repoContentDao: RepoReadmeDao
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

    override suspend fun saveRepoLanguages(
        repoLanguageEntity: RepoLanguageEntity
    ) {
        try {
            repoLanguageDao.insert(repoLanguageEntity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun saveRepoContent(repoContentEntity: RepoReadmeEntity) {
        try {
        repoContentDao.insert(repoContentEntity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override suspend fun fetchRepoContent(repoId: Long): Result<RepoReadmeEntity?> {
        try {
            val content = repoContentDao.getByRepoId(repoId = repoId)
            return Result.Success(data = content)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(GitException.UnknownError)
        }
    }

    override suspend fun fetchRepoLanguages(repoId: Long): Result<RepoLanguageEntity?> {
        try {
            val content = repoLanguageDao.getByRepoId(repoId = repoId)
            return Result.Success(data = content)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(GitException.UnknownError)
        }
    }

}