package hopeapps.dedev.feature_users.data.repository

import hopeapps.dedev.feature_users.data.datasource.LocalDataSource
import hopeapps.dedev.feature_users.data.datasource.RemoteDataSource
import hopeapps.dedev.feature_users.data.mapper.entityToUsers
import hopeapps.dedev.feature_users.data.mapper.toUser
import hopeapps.dedev.feature_users.domain.entity.User
import hopeapps.dedev.feature_users.domain.repository.UserRepository

class UserRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
): UserRepository {

    override suspend fun searchUser(userFilterText: String): User {
        val user = remoteDataSource.searchUser(userFilterText).toUser()
        return user
    }

    override suspend fun fetchRecentUsers(): List<User> {
        return localDataSource.fetchRecentUsers().entityToUsers()
    }
}