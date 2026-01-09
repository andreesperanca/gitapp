package hopeapps.dedev.feature_repo.domain.usecase

import androidx.paging.PagingData
import hopeapps.dedev.feature_repo.domain.entity.Issue
import hopeapps.dedev.feature_repo.domain.entity.PullRequest
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
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
class IssuesUseCaseTest {

    private lateinit var repository: RepoRepository
    private lateinit var useCase: IssueUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = IssueUseCase(repository)
    }

    @Test
    fun `invoke fetchIssuesPaginated should delegate call to repository`() = runTest {
        val pagingFlow = flowOf(PagingData.empty<Issue>())

        every {
            repository.fetchIssuesPaginated(
                repoName = "teste",
                repoOwner = "teste",
                repoId = 0
            )
        } returns pagingFlow

        val result = useCase.fetchIssuesPaginated(
            repoName = "teste",
            repoOwner = "teste",
            repoId = 0
        )

        assertSame(pagingFlow, result)

        verify(exactly = 1) {
            repository.fetchIssuesPaginated(
                repoName = "teste",
                repoOwner = "teste",
                repoId = 0
            )
        }
    }
}
