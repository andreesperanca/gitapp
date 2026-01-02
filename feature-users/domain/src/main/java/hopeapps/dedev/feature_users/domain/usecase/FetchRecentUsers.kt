package hopeapps.dedev.feature_users.domain.usecase

import hopeapps.dedev.common.Result
import hopeapps.dedev.feature_users.domain.entity.User
import hopeapps.dedev.feature_users.domain.repository.UserRepository


class FetchRecentUsersUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userFilterText: String = ""
    ): Result<List<User>> {
        val users = userRepository.fetchRecentUsers()
        return users
    }
}