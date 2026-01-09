package hopeapps.dedev.feature_repo.domain.usecase

import androidx.paging.PagingData
import hopeapps.dedev.common.GitException
import hopeapps.dedev.common.Result
import hopeapps.dedev.feature_repo.domain.entity.ForkFilterType
import hopeapps.dedev.feature_repo.domain.entity.RepoReadme
import hopeapps.dedev.feature_repo.domain.entity.RepoSearchFilter
import hopeapps.dedev.feature_repo.domain.entity.RepoSort
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@Suppress("UnusedFlow", "UNUSED_FLOW_INSPECTION")
@OptIn(ExperimentalCoroutinesApi::class)
class RepoUseCaseTest {

    private lateinit var repository: RepoRepository
    private lateinit var useCase: RepoUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = RepoUseCase(repository)
    }

    @Test
    fun `invoke fetchRepositoryPaginated should delegate call to repository`() = runTest {
        val user = "andreesperanca"
        val pagingFlow = flowOf(PagingData.empty<Repository>())

        every {
            repository.fetchRepoPaginated(user)
        } returns pagingFlow

        val result = useCase.fetchRepositoryPaginated(user)

        assertSame(pagingFlow, result)

        verify(exactly = 1) {
            repository.fetchRepoPaginated(user)
        }

        confirmVerified(repository)
    }

    @Test
    fun `invoke searchRepositoryPaginated should delegate search call to repository`() = runTest {
        val filter = RepoSearchFilter(
            user = "",
            language = "",
            forkFilter = ForkFilterType.OnlyForks,
            sort = RepoSort.Forks
        )
        val user = "andreesperanca"

        val pagingFlow = flowOf(PagingData.empty<Repository>())

        every {
            repository.fetchSearchRepoPaginated(
                filter = filter,
                userFilterText = user
            )
        } returns pagingFlow

        val result = useCase.searchRepositoryPaginated(
            filter = filter,
            userFilterText = user
        )

        assertSame(pagingFlow, result)

        verify(exactly = 1) {
            repository.fetchSearchRepoPaginated(
                filter = filter,
                userFilterText = user
            )
        }

        confirmVerified(repository)
    }

    @Test
    fun `fetchRepoById returns Success when repository returns Success`() = runTest {

        val res = Result.Success(
            data = Repository(
                id = 0,
                name = "",
                repoOwner = "",
                stars = 0,
                forks = 0,
                watchers = 0,
                issues = 0,
                description = "",
                language = "",
                lastUpdate = "",
                isFork = false
            )
        )

        coEvery {
            repository.fetchRepoById(repoId = 0)
        } returns res

        val result = useCase.fetchRepoById(repoId = 0)

        coVerify(exactly = 1) {
            repository.fetchRepoById(repoId = 0)
        }

        assert(result is Result.Success)
    }

    @Test
    fun `fetchRepoById returns Error when repository returns Error`() = runTest {
        coEvery {
            repository.fetchRepoById(repoId = 0)
        } returns Result.Error(GitException.UnknownError)

        val result = useCase.fetchRepoById(repoId = 0)

        coVerify(exactly = 1) {
            repository.fetchRepoById(repoId = 0)
        }

        assert(result is Result.Error)
    }

    @Test
    fun `fetchRepoLanguages returns Success when repository returns Success`() = runTest {

        val res = Result.Success(data = listOf(""))

        coEvery {
            repository.fetchRepoLanguages(repoOwner = "teste", repoName = "teste", repoId = 0)
        } returns res

        val result = useCase.fetchRepoLanguages(repoOwner = "teste", repoName = "teste", repoId = 0)

        coVerify(exactly = 1) {
            repository.fetchRepoLanguages(repoOwner = "teste", repoName = "teste", repoId = 0)
        }

        assert(result is Result.Success)
    }

    @Test
    fun `fetchRepoLanguages returns Error when repository returns Error`() = runTest {


        coEvery {
            repository.fetchRepoLanguages(repoOwner = "teste", repoName = "teste", repoId = 0)
        } returns Result.Error(error = GitException.UnknownError)

        val result = useCase.fetchRepoLanguages(repoOwner = "teste", repoName = "teste", repoId = 0)

        coVerify(exactly = 1) {
            repository.fetchRepoLanguages(repoOwner = "teste", repoName = "teste", repoId = 0)
        }

        assert(result is Result.Error)
    }

    @Test
    fun `fetchRepositoryReadme returns Success when repository returns Success`() = runTest {

        val res = Result.Success(data = RepoReadme(content = ""))

        coEvery {
            repository.fetchRepositoryReadme(repoOwner = "teste", repoName = "teste", repoId = 0)
        } returns res

        val result = useCase.fetchRepositoryReadme(repoOwner = "teste", repoName = "teste", repoId = 0)

        coVerify(exactly = 1) {
            repository.fetchRepositoryReadme(repoOwner = "teste", repoName = "teste", repoId = 0)
        }

        assert(result is Result.Success)
    }

    @Test
    fun `fetchRepositoryReadme returns Error when repository returns Error`() = runTest {

        val res = Result.Success(data = RepoReadme(content = ""))

        coEvery {
            repository.fetchRepositoryReadme(repoOwner = "teste", repoName = "teste", repoId = 0)
        } returns res

        val result = useCase.fetchRepositoryReadme(repoOwner = "teste", repoName = "teste", repoId = 0)

        coVerify(exactly = 1) {
            repository.fetchRepositoryReadme(repoOwner = "teste", repoName = "teste", repoId = 0)
        }

        assert(result is Result.Success)
    }

}
