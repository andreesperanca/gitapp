package hopeapps.dedev.feature_users.domain.repository

import hopeapps.dedev.feature_users.domain.entity.User

interface UserRepository {
    suspend fun searchUser(userFilterText: String): User
    suspend fun fetchRecentUsers(): List<User>
}