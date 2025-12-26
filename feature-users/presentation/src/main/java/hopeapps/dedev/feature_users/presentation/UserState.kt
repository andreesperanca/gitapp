package hopeapps.dedev.feature_users.presentation

import hopeapps.dedev.feature_users.domain.entity.User

data class UserState(
    val recentUsers: List<User> = emptyList()
)