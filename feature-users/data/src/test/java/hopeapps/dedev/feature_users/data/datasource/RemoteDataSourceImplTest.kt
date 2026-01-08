package hopeapps.dedev.feature_users.data.datasource

import hopeapps.dedev.core.network.GitApi
import hopeapps.dedev.core.network.models.UserDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException
import hopeapps.dedev.common.*

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteDataSourceImplTest {

    private val gitApi: GitApi = mockk()
    private lateinit var remoteDataSource: UserRemoteDataSource

    @Before
    fun setup() {
        remoteDataSource = RemoteDataSourceImpl(gitApi)
    }


    @Test
    fun `searchUser should return Success when api call succeeds`() = runTest {
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

        coEvery {
            gitApi.fetchRemoteUser("andre")
        } returns dtoUser

        val result = remoteDataSource.searchUser("andre")

        assertTrue(result is Result.Success)
        assertEquals(dtoUser, (result as Result.Success).data)

        coVerify(exactly = 1) {
            gitApi.fetchRemoteUser("andre")
        }
    }

    @Test
    fun `searchUser should return NetworkError when api throws IOException`() = runTest {
        coEvery {
            gitApi.fetchRemoteUser(any())
        } throws IOException()

        val result = remoteDataSource.searchUser("andre")

        assertTrue(result is Result.Error)
        assertEquals(
            GitException.NetworkError,
            (result as Result.Error).error
        )
    }

    @Test
    fun `searchUser should return UnknownError when api throws Exception`() = runTest {
        coEvery {
            gitApi.fetchRemoteUser(any())
        } throws RuntimeException("boom")

        val result = remoteDataSource.searchUser("andre")

        assertTrue(result is Result.Error)
        assertEquals(
            GitException.UnknownError,
            (result as Result.Error).error
        )
    }
}