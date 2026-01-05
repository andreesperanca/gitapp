package hopeapps.dedev.feature_users.domain.usecase

import hopeapps.dedev.common.Result
import hopeapps.dedev.feature_users.domain.entity.User
import hopeapps.dedev.feature_users.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SearchUserUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userFilterText: String
    ): Result<User> {
        return withContext(Dispatchers.IO) {
            userRepository.searchUser(userFilterText)
        }
    }

}