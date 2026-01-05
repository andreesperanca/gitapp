package hopeapps.dedev.feature_repo.domain.usecase

import androidx.paging.PagingData
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
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
class FetchRepositoryPaginatedUseCaseTest {

    private lateinit var repository: RepoRepository
    private lateinit var useCase: FetchRepositoryPaginatedUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = FetchRepositoryPaginatedUseCase(repository)
    }

    @Test
    fun `invoke should delegate call to repository`() = runTest {
        val user = "andreesperanca"
        val pagingFlow = flowOf(PagingData.empty<Repository>())

        every {
            repository.fetchRepoPaginated(user)
        } returns pagingFlow

        val result = useCase.invoke(user)

        assertSame(pagingFlow, result)

        verify(exactly = 1) {
            repository.fetchRepoPaginated(user)
        }

        confirmVerified(repository)
    }
}
