package hopeapps.dedev.feature_users.data.datasource

import hopeapps.dedev.core.database.model.UserEntity

interface LocalDataSource {
    suspend fun fetchRecentUsers(): List<UserEntity>
}