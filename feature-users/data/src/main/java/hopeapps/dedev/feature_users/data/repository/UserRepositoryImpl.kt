package hopeapps.dedev.feature_users.data.repository

import hopeapps.dedev.common.Result
import hopeapps.dedev.feature_users.data.datasource.LocalDataSource
import hopeapps.dedev.feature_users.data.datasource.RemoteDataSource
import hopeapps.dedev.feature_users.data.mapper.entityToUsers
import hopeapps.dedev.feature_users.data.mapper.toDomain
import hopeapps.dedev.feature_users.data.mapper.toEntity
import hopeapps.dedev.feature_users.domain.entity.User
import hopeapps.dedev.feature_users.domain.repository.UserRepository

class UserRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
): UserRepository {

    override suspend fun searchUser(userFilterText: String): Result<User> {
        val response = remoteDataSource.searchUser(userFilterText)
        return when (response) {
            is Result.Error -> {
                Result.Error(error = response.error)
            }

            is Result.Success -> {
                localDataSource.saveUser(response.data.toEntity())
                Result.Success(data = response.data.toDomain())
            }
        }
    }

    override suspend fun fetchRecentUsers(): Result<List<User>> {
        val response = localDataSource.fetchRecentUsers()
        return when (response) {
            is Result.Error -> {
                Result.Error(error = response.error)
            }

            is Result.Success -> {
                Result.Success(data = response.data.entityToUsers())
            }
        }
    }
}