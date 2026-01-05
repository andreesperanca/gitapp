package hopeapps.dedev.feature_repo.presentation.search

import androidx.paging.PagingData
import app.cash.turbine.test
import hopeapps.dedev.common.MainDispatcherRule
import hopeapps.dedev.feature_repo.domain.entity.RepoSearchFilter
import hopeapps.dedev.feature_repo.domain.entity.RepoSort
import hopeapps.dedev.feature_repo.domain.usecase.SearchRepositoryPaginatedUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@Suppress("UnusedFlow", "UNUSED_FLOW_INSPECTION")
class RepoSearchViewModelTest {


    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val useCase: SearchRepositoryPaginatedUseCase = mockk()
    private lateinit var viewModel: RepoSearchViewModel


    @Test
    fun `init should update user and trigger search`() = runTest {
        val user = "andreesperanca"
        val filter = RepoSearchFilter(user = user)

        every {
            useCase(
                filter = filter,
                userFilterText = user
            )
        } returns flowOf(PagingData.empty())

        viewModel = RepoSearchViewModel(useCase)

        viewModel.init(user)

        viewModel.repoPagingFlow.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        verify {
            useCase(
                filter = match { it.user == user },
                userFilterText = user
            )
        }
    }

    @Test
    fun `UpdateSortFilter Stars should update filter`() = runTest {
        viewModel = RepoSearchViewModel(useCase)

        viewModel.onAction(
            RepoSearchAction.UpdateSortFilter(RepoSort.Stars)
        )

        assertEquals(
            RepoSort.Stars,
            viewModel.repoSearchFilter.value.sort
        )
    }
}