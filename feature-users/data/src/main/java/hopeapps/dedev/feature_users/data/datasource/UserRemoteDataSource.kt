package hopeapps.dedev.feature_users.data.datasource

import hopeapps.dedev.common.Result
import hopeapps.dedev.core.network.models.UserDto

interface UserRemoteDataSource {
    suspend fun searchUser(userFilterText: String): Result<UserDto>
}