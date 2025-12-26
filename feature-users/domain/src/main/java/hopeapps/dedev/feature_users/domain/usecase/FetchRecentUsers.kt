package hopeapps.dedev.feature_users.domain.usecase

import hopeapps.dedev.feature_users.domain.entity.User
import hopeapps.dedev.feature_users.domain.repository.UserRepository


class FetchRecentUsersUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userFilterText: String = ""
    ): List<User> {

        val users = userRepository.fetchRecentUsers()

        if (userFilterText.isEmpty()) {
            return users
        }

        return users.filter { user ->
            user.login.contains(userFilterText)
        }
    }
}