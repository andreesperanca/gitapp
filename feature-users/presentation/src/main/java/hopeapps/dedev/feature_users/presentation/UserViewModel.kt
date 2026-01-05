package hopeapps.dedev.feature_users.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hopeapps.dedev.common.Result
import hopeapps.dedev.common.launchSuspend
import hopeapps.dedev.feature_users.domain.entity.User
import hopeapps.dedev.feature_users.domain.usecase.FetchRecentUsersUseCase
import hopeapps.dedev.feature_users.domain.usecase.SearchUserUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    private val searchUsersUseCase: SearchUserUseCase,
    private val fetchRecentUsersUseCase: FetchRecentUsersUseCase
) : ViewModel() {

    var state = MutableStateFlow(UserState())
        private set

    var event = MutableSharedFlow<UserEvent>()
        private set

    init {
        fetchHistoricUsers()
    }

    fun onAction(action: UserAction) {
        when (action) {
            is UserAction.FilterClick -> handleFilterClick(action.userFilterText)
            is UserAction.ExpandedSearchClick -> handleUpdateExpandedSearch(action.boolean)
            is UserAction.UpdateFilterText -> handleUpdateFilterText(action.userFilterText)
            is UserAction.SelectedUser -> handleSelectedUser(action.user)
        }
    }

    private fun handleFilterClick(userFilterText: String) {
        viewModelScope.launchSuspend(
            block = {
                searchUsersUseCase(userFilterText)
            },
            onLoading = { isLoading ->
                state.update { state ->
                    state.copy(
                        isLoading = isLoading
                    )
                }
            },
            onResult = { response ->
                when (response) {
                    is Result.Error -> {
                        sendEvent(UserEvent.ShowSnackBar(message = "Error message!"))
                    }
                    is Result.Success -> {
                        fetchHistoricUsers()
                    }
                }
            }
        )
    }

    private fun handleUpdateExpandedSearch(isExpandedSearch: Boolean) {
        viewModelScope.launch {
            state.update { state ->
                state.copy(
                    isSearchExpanded = isExpandedSearch
                )
            }
        }
    }

    private fun handleUpdateFilterText(userFilterText: String) {
        viewModelScope.launch {
            state.update { state ->
                state.copy(
                    filterText = userFilterText
                )
            }
        }
    }

    private fun handleSelectedUser(user: User) {
        sendEvent(UserEvent.UserSelected(user))
    }

    private fun sendEvent(newEvent: UserEvent) {
        viewModelScope.launch {
            event.emit(newEvent)
        }
    }

    private fun fetchHistoricUsers() {
        viewModelScope.launchSuspend(
            onLoading = { isLoading ->
                state.update { state -> state.copy(isLoading = isLoading) }
            },
            block = { fetchRecentUsersUseCase() },
            onResult = { response ->
                when (response) {
                    is Result.Error -> {
                        sendEvent(UserEvent.ShowSnackBar(message = "Error message!"))
                    }
                    is Result.Success -> {
                        state.update { it.copy(recentUsers = response.data) }
                    }
                }
            }
        )
    }
}