package hopeapps.dedev.feature_repo.data.datasource

import hopeapps.dedev.common.GitException
import hopeapps.dedev.common.Result
import hopeapps.dedev.core.network.GitApi
import hopeapps.dedev.core.network.models.RepoReadmeDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException


@OptIn(ExperimentalCoroutinesApi::class)
class RepoRemoteDataSourceTest {

    private lateinit var datasource: RepoRemoteDataSource
    private val gitApi = mockk<GitApi>()

    @Before
    fun setup() {
        datasource = RepoRemoteDataSourceImpl(gitApi)
    }

    @Test
    fun `fetchRepositoryReadme returns Success when api returns readme`() = runTest {
        val owner = "android"
        val repo = "compose"

        val readme = RepoReadmeDto(
            content = "README"
        )

        coEvery {
            gitApi.fetchReadmeRepository(owner, repo)
        } returns readme

        val result = datasource.fetchRepositoryReadme(owner, repo)

        assert(result is Result.Success)
        assert((result as Result.Success).data == readme)

        coVerify(exactly = 1) {
            gitApi.fetchReadmeRepository(owner, repo)
        }

        confirmVerified(gitApi)
    }

    @Test
    fun `fetchRepositoryReadme returns NetworkError when api throws IOException`() = runTest {
        val owner = "android"
        val repo = "compose"

        coEvery {
            gitApi.fetchReadmeRepository(owner, repo)
        } throws IOException()

        val result = datasource.fetchRepositoryReadme(owner, repo)

        assert(result is Result.Error)
        assert((result as Result.Error).error == GitException.NetworkError)

        coVerify(exactly = 1) {
            gitApi.fetchReadmeRepository(owner, repo)
        }

        confirmVerified(gitApi)
    }

    @Test
    fun `fetchRepositoryReadme returns UnknownError when api throws Exception`() = runTest {
        val owner = "android"
        val repo = "compose"

        coEvery {
            gitApi.fetchReadmeRepository(owner, repo)
        } throws RuntimeException("Unexpected error")

        val result = datasource.fetchRepositoryReadme(owner, repo)

        assert(result is Result.Error)
        assert((result as Result.Error).error == GitException.UnknownError)

        coVerify(exactly = 1) {
            gitApi.fetchReadmeRepository(owner, repo)
        }

        confirmVerified(gitApi)
    }

    @Test
    fun `fetchRepositoryLanguages returns Success when api returns readme`() = runTest {
        val owner = "android"
        val repo = "compose"

        val languages:  Map<String, Int> = mapOf( "Kotlin" to 1, "Java" to 1)

        coEvery {
            gitApi.getRepositoryLanguages(owner, repo)
        } returns languages

        val result = datasource.fetchRepositoryLanguages(owner, repo)

        assert(result is Result.Success)
        assert((result as Result.Success).data == languages)

        coVerify(exactly = 1) {
            gitApi.getRepositoryLanguages(owner, repo)
        }

        confirmVerified(gitApi)
    }

    @Test
    fun `fetchRepositoryLanguages returns NetworkError when api throws IOException`() = runTest {
        val owner = "android"
        val repo = "compose"

        coEvery {
            gitApi.getRepositoryLanguages(owner, repo)
        } throws IOException()

        val result = datasource.fetchRepositoryLanguages(owner, repo)

        assert(result is Result.Error)
        assert((result as Result.Error).error == GitException.NetworkError)

        coVerify(exactly = 1) {
            gitApi.getRepositoryLanguages(owner, repo)
        }

        confirmVerified(gitApi)
    }

    @Test
    fun `fetchRepositoryLanguages returns UnknownError when api throws Exception`() = runTest {
        val owner = "android"
        val repo = "compose"

        coEvery {
            gitApi.getRepositoryLanguages(owner, repo)
        } throws RuntimeException("Unexpected error")

        val result = datasource.fetchRepositoryLanguages(owner, repo)

        assert(result is Result.Error)
        assert((result as Result.Error).error == GitException.UnknownError)

        coVerify(exactly = 1) {
            gitApi.getRepositoryLanguages(owner, repo)
        }

        confirmVerified(gitApi)
    }
}
