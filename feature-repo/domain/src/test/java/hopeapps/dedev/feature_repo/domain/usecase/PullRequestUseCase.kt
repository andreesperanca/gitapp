package hopeapps.dedev.feature_repo.domain.usecase

import androidx.paging.PagingData
import hopeapps.dedev.common.GitException
import hopeapps.dedev.common.Result
import hopeapps.dedev.feature_repo.domain.entity.ForkFilterType
import hopeapps.dedev.feature_repo.domain.entity.PullRequest
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
class PullRequestUseCaseTest {

    private lateinit var repository: RepoRepository
    private lateinit var useCase: PullRequestUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = PullRequestUseCase(repository)
    }

    @Test
    fun `invoke fetchPullRequestsPaginated should delegate call to repository`() = runTest {
        val pagingFlow = flowOf(PagingData.empty<PullRequest>())

        every {
            repository.fetchPullRequestsPaginated(
                repoName = "teste",
                repoOwner = "teste",
                repoId = 0
            )
        } returns pagingFlow

        val result = useCase.fetchPullRequestsPaginated(
            repoName = "teste",
            repoOwner = "teste",
            repoId = 0
        )

        assertSame(pagingFlow, result)

        verify(exactly = 1) {
            repository.fetchPullRequestsPaginated(
                repoName = "teste",
                repoOwner = "teste",
                repoId = 0
            )
        }
    }

}
