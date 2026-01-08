package hopeapps.dedev.feature_users.data.repository

import hopeapps.dedev.common.Result
import hopeapps.dedev.feature_users.data.datasource.UserLocalDataSource
import hopeapps.dedev.feature_users.data.datasource.UserRemoteDataSource
import hopeapps.dedev.feature_users.data.mapper.toDomain
import hopeapps.dedev.feature_users.data.mapper.toEntity
import hopeapps.dedev.feature_users.domain.entity.User
import hopeapps.dedev.feature_users.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
): UserRepository {

    override suspend fun searchUser(userFilterText: String): Result<User> {
        val response = userRemoteDataSource.searchUser(userFilterText)
        return when (response) {
            is Result.Error -> { Result.Error(error = response.error) }
            is Result.Success -> {
                userLocalDataSource.saveUser(response.data.toEntity())
                Result.Success(data = response.data.toDomain())
            }
        }
    }

    override suspend fun fetchRecentUsers(): Result<List<User>> {
        val response = userLocalDataSource.fetchRecentUsers()
        return when (response) {
            is Result.Error -> { Result.Error(error = response.error) }
            is Result.Success -> { Result.Success(data = response.data.toDomain()) }
        }
    }
}