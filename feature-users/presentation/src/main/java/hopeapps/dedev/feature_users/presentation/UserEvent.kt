package hopeapps.dedev.feature_users.presentation

import hopeapps.dedev.feature_users.domain.entity.User

interface UserEvent {
    data class UserSelected(val user: User) : UserEvent
}