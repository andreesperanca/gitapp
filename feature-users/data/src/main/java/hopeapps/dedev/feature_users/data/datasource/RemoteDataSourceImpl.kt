package hopeapps.dedev.feature_users.data.datasource

import hopeapps.dedev.common.Result
import hopeapps.dedev.common.safeApiCall
import hopeapps.dedev.core.network.GitApi
import hopeapps.dedev.core.network.models.UserDto

class RemoteDataSourceImpl(
    val gitApi: GitApi
): UserRemoteDataSource {

    override suspend fun searchUser(userFilterText: String): Result<UserDto> {
        return safeApiCall {
            gitApi.fetchRemoteUser(userFilterText)
        }
    }
}