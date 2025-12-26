package hopeapps.dedev.feature_users.data.datasource

import hopeapps.dedev.core.network.models.UserDto

interface RemoteDataSource {
    suspend fun searchUser(userFilterText: String): UserDto
}