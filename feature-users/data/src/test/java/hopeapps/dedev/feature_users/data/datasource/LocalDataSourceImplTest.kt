package hopeapps.dedev.feature_users.data.datasource

import hopeapps.dedev.common.GitException
import hopeapps.dedev.core.database.dao.UserDao
import hopeapps.dedev.core.database.model.UserEntity
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class LocalDataSourceImplTest {
    private lateinit var dataSource: LocalDataSourceImpl
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        userDao = mockk()
        dataSource = LocalDataSourceImpl(userDao)
    }

    @Test
    fun `fetchRecentUsers should return success when dao returns users`() = runTest {
        val users = listOf(
            UserEntity(
                login = "andre",
                avatarUrl = "url",
                name = "Andre",
                bio = "dev",
                followers = 1,
                following = 1,
                publicRepos = 1,
                id = 1
            )
        )

        coEvery {
            userDao.fetchUsers()
        } returns users

        val result = dataSource.fetchRecentUsers()

        assertTrue(result is hopeapps.dedev.common.Result.Success)
        assertEquals(users, (result as hopeapps.dedev.common.Result.Success).data)
    }

    @Test
    fun `fetchRecentUsers should return error when dao throws exception`() = runTest {
        coEvery {
            userDao.fetchUsers()
        } throws RuntimeException("DB error")

        val result = dataSource.fetchRecentUsers()

        assertTrue(result is hopeapps.dedev.common.Result.Error)
        assertEquals(GitException.UnknownError, (result as hopeapps.dedev.common.Result.Error).error)
    }

    @Test
    fun `saveUser should call dao save`() = runTest {
        val user = UserEntity(
            login = "andre",
            avatarUrl = "url",
            name = "Andre",
            bio = "dev",
            followers = 1,
            following = 1,
            publicRepos = 1,
            id = 0
        )

        coEvery {
            userDao.saveRecentUsers(user)
        } just Runs

        dataSource.saveUser(user)

        coVerify(exactly = 1) {
            userDao.saveRecentUsers(user)
        }
    }

}