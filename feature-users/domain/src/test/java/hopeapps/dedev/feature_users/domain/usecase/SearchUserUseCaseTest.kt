package hopeapps.dedev.feature_users.domain.usecase

import hopeapps.dedev.common.GitException
import hopeapps.dedev.feature_users.domain.entity.User
import hopeapps.dedev.feature_users.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class SearchUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: SearchUserUseCase

    @Before
    fun setup() {
        userRepository = mockk()
        useCase = SearchUserUseCase(userRepository)
    }

    @Test
    fun `invoke should return Success when repository returns user`() = runTest {
        val user = User(
            login = "andre",
            avatarUrl = "url",
            name = "Andre",
            bio = "dev",
            followers = 1,
            following = 1,
            publicRepos = 1
        )

        coEvery {
            userRepository.searchUser("andre")
        } returns hopeapps.dedev.common.Result.Success(user)

        val result = useCase("andre")

        assertTrue(result is hopeapps.dedev.common.Result.Success)
        assertEquals(user, (result as hopeapps.dedev.common.Result.Success).data)

        coVerify(exactly = 1) {
            userRepository.searchUser("andre")
        }
    }

    @Test
    fun `invoke should return Error with correct NetworkError GitException when repository throw network exception`() = runTest {

       val exception = GitException.NetworkError

        coEvery {
            userRepository.searchUser("andre")
        } returns hopeapps.dedev.common.Result.Error(GitException.NetworkError)

        val result = useCase("andre")

        assertTrue(result is hopeapps.dedev.common.Result.Error)
        assertEquals(exception, (result as hopeapps.dedev.common.Result.Error).error)

        coVerify(exactly = 1) {
            userRepository.searchUser("andre")
        }
    }

    @Test
    fun `invoke should return Error with correct UnknownError GitException when repository throw unknown exception`() = runTest {

        val exception = GitException.UnknownError

        coEvery {
            userRepository.searchUser("andre")
        } returns hopeapps.dedev.common.Result.Error(GitException.UnknownError)

        val result = useCase("andre")

        assertTrue(result is hopeapps.dedev.common.Result.Error)
        assertEquals(exception, (result as hopeapps.dedev.common.Result.Error).error)

        coVerify(exactly = 1) {
            userRepository.searchUser("andre")
        }
    }


}