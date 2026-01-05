package hopeapps.dedev.feature_repo.presentation.list

import androidx.paging.PagingData
import app.cash.turbine.test
import hopeapps.dedev.common.MainDispatcherRule
import hopeapps.dedev.feature_repo.domain.entity.Repository
import hopeapps.dedev.feature_repo.domain.usecase.FetchRepositoryPaginatedUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("UnusedFlow", "UNUSED_FLOW_INSPECTION")
class RepositoriesViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var useCase: FetchRepositoryPaginatedUseCase
    private lateinit var viewModel: RepositoriesViewModel

    @Before
    fun setup() {
        useCase = mockk()
        viewModel = RepositoriesViewModel(useCase)
    }


    @Test
    fun `init should set userLogin and trigger search`() = runTest {
        val login = "andreesperanca"

        val pagingData = PagingData.from(
            listOf(
                Repository(
                    id = 1,
                    name = "Repo Test",
                    description = "Description",
                    stars = 1,
                    forks = 1,
                    language = "Java",
                    lastUpdate = "LastUpdate",
                    isFork = false
                )
            )
        )

        every { useCase.invoke(login) } returns flowOf(pagingData)

        viewModel.init(login)

        assertEquals(login, viewModel.userLogin)

        viewModel.repoPagingFlow.test {
            val item = awaitItem()
            assertNotNull(item)
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) {
            useCase.invoke(login)
        }
    }

    @Test
    fun `searchRepositories should update paging flow`() = runTest {
        val login = "andreesperanca"

        every { useCase.invoke(login) } returns flowOf(PagingData.empty())

        viewModel.searchRepositories(login)

        viewModel.repoPagingFlow.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) {
            useCase(login)
        }
    }

}