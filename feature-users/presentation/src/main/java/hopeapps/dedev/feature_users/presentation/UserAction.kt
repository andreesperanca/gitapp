package hopeapps.dedev.feature_users.presentation

sealed interface UserAction {
    data class FilterClick (val userFilterText: String) : UserAction
}