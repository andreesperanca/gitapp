package hopeapps.dedev.feature_users.data.datasource

import hopeapps.dedev.common.GitException
import hopeapps.dedev.common.Result
import hopeapps.dedev.core.database.dao.UserDao
import hopeapps.dedev.core.database.model.UserEntity

class LocalDataSourceImpl(
    private val userDao: UserDao
) : LocalDataSource {

    override suspend fun fetchRecentUsers(): Result<List<UserEntity>> {
        return try {
            Result.Success(userDao.fetchUsers())
        } catch (e: Exception) {
            Result.Error(GitException.UnknownError)
        }
    }

    override suspend fun saveUser(user: UserEntity) {
        userDao.saveRecentUsers(user)
    }
}