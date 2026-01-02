package hopeapps.dedev.feature_users.presentation

import hopeapps.dedev.feature_users.domain.entity.User

sealed interface UserAction {
    data class FilterClick (val userFilterText: String) : UserAction
    data class ExpandedSearchClick (val boolean: Boolean) : UserAction
    data class UpdateFilterText (val userFilterText: String) : UserAction
    data class SelectedUser (val user: User) : UserAction
}