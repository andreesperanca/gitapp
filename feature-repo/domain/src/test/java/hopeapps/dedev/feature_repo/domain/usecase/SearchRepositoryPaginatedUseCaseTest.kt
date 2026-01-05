package hopeapps.dedev.feature_repo.domain.usecase

import androidx.paging.PagingData
import hopeapps.dedev.feature_repo.domain.entity.ForkFilterType
import hopeapps.dedev.feature_repo.domain.entity.RepoSearchFilter
import hopeapps.dedev.feature_repo.domain.entity.RepoSort
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
class SearchRepositoryPaginatedUseCaseTest {

    private lateinit var repository: RepoRepository
    private lateinit var useCase: SearchRepositoryPaginatedUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = SearchRepositoryPaginatedUseCase(repository)
    }

    @Test
    fun `invoke should delegate search call to repository`() = runTest {
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

        val result = useCase.invoke(
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

}
