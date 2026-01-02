package hopeapps.dedev.feature_users.data.datasource

import hopeapps.dedev.common.Result
import hopeapps.dedev.core.database.model.UserEntity

interface LocalDataSource {
    suspend fun fetchRecentUsers(): Result<List<UserEntity>>
    suspend fun saveUser(user: UserEntity)
}