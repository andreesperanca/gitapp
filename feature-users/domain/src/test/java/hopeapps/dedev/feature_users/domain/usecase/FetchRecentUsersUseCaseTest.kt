package hopeapps.dedev.feature_users.domain.usecase

import hopeapps.dedev.common.GitException
import hopeapps.dedev.common.Result
import hopeapps.dedev.common.Result.Success
import hopeapps.dedev.feature_users.domain.entity.User
import hopeapps.dedev.feature_users.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class FetchRecentUsersUseCaseTest {

    private val userRepository: UserRepository = mockk()
    private lateinit var useCase: FetchRecentUsersUseCase

    @Before
    fun setup() {
        useCase = FetchRecentUsersUseCase(
            userRepository = userRepository
        )
    }

    @Test
    fun `should fetch users using repository`() = runTest {
        val user = User(login = "", avatarUrl = "", name = "", bio = "", followers = 1, following = 1, publicRepos = 1)
        val users = listOf(user)

        coEvery { userRepository.fetchRecentUsers() } returns Success(users)

        val result = useCase()
        advanceUntilIdle()

        assertEquals(users, (result as Success).data)
    }

    @Test
    fun `should return result with Git Network exception when repository throws network exception`() = runTest {
        val exception = GitException.NetworkError

        coEvery { userRepository.fetchRecentUsers() } returns Result.Error(exception)

        val result = useCase()
        advanceUntilIdle()

        assertEquals(exception, (result as Result.Error).error)
    }


    @Test
    fun `should return result with Git Network exception when repository throws unknown exception`() = runTest {
        val exception = GitException.UnknownError

        coEvery { userRepository.fetchRecentUsers() } returns Result.Error(exception)

        val result = useCase()
        advanceUntilIdle()

        assertEquals(exception, (result as Result.Error).error)
    }

}
