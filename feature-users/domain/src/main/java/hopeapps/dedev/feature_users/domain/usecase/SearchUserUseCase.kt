package hopeapps.dedev.feature_users.domain.usecase

import hopeapps.dedev.feature_users.domain.entity.User
import hopeapps.dedev.feature_users.domain.repository.UserRepository


class SearchUserUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userFilterText: String
    ): User {
        return userRepository.searchUser(userFilterText)
    }

}