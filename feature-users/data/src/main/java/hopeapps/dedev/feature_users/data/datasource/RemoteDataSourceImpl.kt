package hopeapps.dedev.feature_users.data.datasource

import hopeapps.dedev.core.network.UserApi
import hopeapps.dedev.core.network.models.UserDto

class RemoteDataSourceImpl(
    val userApi: UserApi
): RemoteDataSource {

    override suspend fun searchUser(userFilterText: String): UserDto {
        return userApi.fetchRemoteUser(userFilterText)
    }

}