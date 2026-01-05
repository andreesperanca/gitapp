package hopeapps.dedev.feature_users.presentation

import app.cash.turbine.test
import hopeapps.dedev.common.GitException
import hopeapps.dedev.common.MainDispatcherRule
import hopeapps.dedev.common.Result
import hopeapps.dedev.feature_users.domain.entity.User
import hopeapps.dedev.feature_users.domain.usecase.FetchRecentUsersUseCase
import hopeapps.dedev.feature_users.domain.usecase.SearchUserUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val searchUsersUseCase: SearchUserUseCase = mockk()
    private val fetchRecentUsersUseCase: FetchRecentUsersUseCase = mockk()

    private lateinit var viewModel: UserViewModel

    @Test
    fun `init should fetch recent users and update state`() = runTest {
        val users = listOf(User(login = "", avatarUrl = "", name = "", bio = "", followers = 1, following = 1, publicRepos = 1))

        coEvery {
            fetchRecentUsersUseCase()
        } returns Result.Success(users)

        viewModel = UserViewModel(
            searchUsersUseCase,
            fetchRecentUsersUseCase
        )
        advanceUntilIdle()

        assertEquals(users, viewModel.state.value.recentUsers)
    }

    @Test
    fun `FilterClick error should emit snackBar event`() = runTest {
        coEvery {
            fetchRecentUsersUseCase()
        } returns Result.Success(emptyList())

        coEvery {
            searchUsersUseCase("andre")
        } returns Result.Error(GitException.UnknownError)

        viewModel = UserViewModel(
            searchUsersUseCase,
            fetchRecentUsersUseCase,
        )

        viewModel.event.test {
            viewModel.onAction(UserAction.FilterClick("andre"))

            val event = awaitItem()
            assertTrue(event is UserEvent.ShowSnackBar)
        }
    }

    @Test
    fun `FilterClick success should fetch historic users`() = runTest {
        val user = User(
            login = "",
            avatarUrl = "",
            name = "",
            bio = "",
            followers = 1,
            following = 1,
            publicRepos = 1
        )
        val users = listOf(user)

        coEvery {
            searchUsersUseCase("andre")
        } returns Result.Success(data = user)

        coEvery {
            fetchRecentUsersUseCase()
        } returns Result.Success(users)

        viewModel = UserViewModel(
            searchUsersUseCase,
            fetchRecentUsersUseCase
        )

        viewModel.onAction(UserAction.FilterClick("andre"))

        assertEquals(users, viewModel.state.value.recentUsers)
    }
}
