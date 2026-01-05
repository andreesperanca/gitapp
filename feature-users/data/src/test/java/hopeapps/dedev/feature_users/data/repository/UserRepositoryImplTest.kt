package hopeapps.dedev.feature_users.data.repository

import hopeapps.dedev.common.GitException
import hopeapps.dedev.common.Result
import hopeapps.dedev.core.database.model.UserEntity
import hopeapps.dedev.core.network.models.UserDto
import hopeapps.dedev.feature_users.data.datasource.LocalDataSource
import hopeapps.dedev.feature_users.data.datasource.RemoteDataSource
import hopeapps.dedev.feature_users.data.mapper.entityToUsers
import hopeapps.dedev.feature_users.data.mapper.toEntity
import hopeapps.dedev.feature_users.domain.entity.User
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
class UserRepositoryImplTest {

    private lateinit var repository: UserRepositoryImpl
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setup() {
        remoteDataSource = mockk()
        localDataSource = mockk(relaxed = true)

        repository = UserRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource
        )
    }

    @Test
    fun `searchUser should save user locally and return domain user when remote success`() = runTest {
        val userFilter = "andre"

        val dtoUser = UserDto(
            login = "andre",
            avatarUrl = "url",
            name = "Andre",
            bio = "dev",
            followers = 1,
            following = 10,
            publicRepos = 1,
            location = "location",
            blog = "blog",
            twitterUsername = "twitter",
            company = "company",
            email = "email",
            createdAt = "createdAt",
            updatedAt = "updatedAt",
            publicGists = 1,
            htmlUrl = "htmlUrl",
            hireable = true,
            nodeId = "",
            id = 1,
            gravatarId = "",
            url = "",
            eventsUrl = "",
            followersUrl = "",
            followingUrl = "",
            gistsUrl = "",
            starredUrl = "",
            subscriptionsUrl = "",
            organizationsUrl = "",
            receivedEventsUrl = "",
            reposUrl = "",
            siteAdmin = false,
            type = "",
            userViewType = ""
        )

        val domainUser = User(
            login = "andre",
            avatarUrl = "url",
            name = "Andre",
            bio = "dev",
            followers = 1,
            publicRepos = 2,
            following = 10
        )

        coEvery {
            remoteDataSource.searchUser(userFilter)
        } returns hopeapps.dedev.common.Result.Success(dtoUser)

        val result = repository.searchUser(userFilter)

        assertTrue(result is hopeapps.dedev.common.Result.Success)
        assertEquals(domainUser.following, (result as Result.Success).data.following)

        coVerify(exactly = 1) {
            localDataSource.saveUser(dtoUser.toEntity())
        }
    }

    @Test
    fun `searchUser should not save user and return error when remote fails`() = runTest {
        val networkError = GitException.NetworkError

        coEvery {
            remoteDataSource.searchUser("andre")
        } returns Result.Error(networkError)

        val result = repository.searchUser("andre")

        assertTrue(result is Result.Error)
        assertEquals(networkError, (result as Result.Error).error)

        coVerify(exactly = 0) {
            localDataSource.saveUser(any())
        }
    }

    @Test
    fun `fetchRecentUsers should return domain users when local success`() = runTest {
        val entities = listOf(
            UserEntity(
                login = "andre",
                avatarUrl = "url",
                name = "Andre",
                bio = "dev",
                followers = 1,
                following = 1,
                publicRepos = 1,
                id = 0
            )
        )

        coEvery {
            localDataSource.fetchRecentUsers()
        } returns Result.Success(entities)

        val result = repository.fetchRecentUsers()

        assertTrue(result is Result.Success)
        assertEquals(entities.entityToUsers(), (result as Result.Success).data)
    }

    @Test
    fun `fetchRecentUsers should return error when local fails`() = runTest {
        val error = GitException.UnknownError

        coEvery {
            localDataSource.fetchRecentUsers()
        } returns Result.Error(error)

        val result = repository.fetchRecentUsers()

        assertTrue(result is Result.Error)
        assertEquals(error, (result as Result.Error).error)
    }

}