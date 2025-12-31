package hopeapps.dedev.feature_users.data.datasource

import hopeapps.dedev.core.network.GitApi
import hopeapps.dedev.core.network.models.RepositoryDto
import hopeapps.dedev.core.network.models.UserDto

class RemoteDataSourceImpl(
    val gitApi: GitApi
): RemoteDataSource {

    override suspend fun searchUser(userFilterText: String): UserDto {
        return gitApi.fetchRemoteUser(userFilterText)
    }
}