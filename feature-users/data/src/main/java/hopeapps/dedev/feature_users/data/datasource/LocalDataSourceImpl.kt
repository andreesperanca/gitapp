package hopeapps.dedev.feature_users.data.datasource

import hopeapps.dedev.core.database.dao.UserDao
import hopeapps.dedev.core.database.model.UserEntity

class LocalDataSourceImpl(
    private val userDao: UserDao
): LocalDataSource {

    override suspend fun fetchRecentUsers(): List<UserEntity> {
        return userDao.fetchUsers()
    }

}