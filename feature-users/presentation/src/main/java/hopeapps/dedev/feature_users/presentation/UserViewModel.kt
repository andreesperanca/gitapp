package hopeapps.dedev.feature_users.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hopeapps.dedev.feature_users.domain.usecase.FetchRecentUsersUseCase
import hopeapps.dedev.feature_users.domain.usecase.SearchUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    private val searchUsersUseCase: SearchUserUseCase,
    private val fetchRecentUsers: FetchRecentUsersUseCase
) : ViewModel() {

    var state = MutableStateFlow(UserState())
        private set

    var event = MutableSharedFlow<UserEvent>()
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            state.update { state ->
                state.copy(
                    recentUsers = fetchRecentUsers()
                )
            }
        }
    }


    fun onAction(action: UserAction) {
        when(action) {
            is UserAction.FilterClick -> handleFilterClick(action.userFilterText)
        }
    }

    private fun handleFilterClick(userFilterText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = searchUsersUseCase(userFilterText)
            state.update { state ->
                state.copy(
                    recentUsers = state.recentUsers + user
                )
            }

        }
    }

}