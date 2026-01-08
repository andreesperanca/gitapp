package hopeapps.dedev.feature_users.data.datasource

import hopeapps.dedev.common.Result
import hopeapps.dedev.core.database.model.UserEntity

interface UserLocalDataSource {
    suspend fun fetchRecentUsers(): Result<List<UserEntity>>
    suspend fun saveUser(user: UserEntity)
}