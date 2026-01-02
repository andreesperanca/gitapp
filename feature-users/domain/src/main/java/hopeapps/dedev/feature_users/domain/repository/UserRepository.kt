package hopeapps.dedev.feature_users.domain.repository

import hopeapps.dedev.common.Result
import hopeapps.dedev.feature_users.domain.entity.User

interface UserRepository {
    suspend fun searchUser(userFilterText: String): Result<User>
    suspend fun fetchRecentUsers(): Result<List<User>>
}