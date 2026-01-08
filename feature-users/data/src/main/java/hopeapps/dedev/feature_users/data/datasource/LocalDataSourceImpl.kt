package hopeapps.dedev.feature_users.data.datasource

import hopeapps.dedev.common.GitException
import hopeapps.dedev.common.Result
import hopeapps.dedev.core.database.dao.UserDao
import hopeapps.dedev.core.database.model.UserEntity

class LocalDataSourceImpl(
    private val userDao: UserDao
) : UserLocalDataSource {

    override suspend fun fetchRecentUsers(): Result<List<UserEntity>> {
        return try {
            Result.Success(userDao.fetchUsers())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(GitException.UnknownError)
        }
    }

    override suspend fun saveUser(user: UserEntity) {
        try {
            userDao.saveRecentUsers(user)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}