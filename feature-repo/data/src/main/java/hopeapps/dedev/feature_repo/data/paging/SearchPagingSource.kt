package hopeapps.dedev.feature_repo.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import hopeapps.dedev.core.network.GitApi
import hopeapps.dedev.core.network.models.RepositoryDto
import hopeapps.dedev.feature_repo.data.mapper.toApiValue
import hopeapps.dedev.feature_repo.domain.entity.RepoSearchFilter


class SearchPagingSource(
    private val gitApi: GitApi,
    private val filter: RepoSearchFilter
) : PagingSource<Int, RepositoryDto>() {

    override fun getRefreshKey(state: PagingState<Int, RepositoryDto>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryDto> {
        val currentPage = params.key ?: 1
        return try {

            val response = gitApi.searchRepositories(
                query = filter.buildQuery(),
                sort = filter.sort.toApiValue(),
                page = currentPage,
                perPage = params.loadSize
            )

            val languageResponse = response.items.forEach {
                gitApi.getRepositoryLanguages(
                    owner = it.owner.login,
                    repo = it.name
                )
            }

            val endOfPaginationReached = response.items.isEmpty()

            if (response.items.isNotEmpty()) {
                LoadResult.Page(
                    data = response.items,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = if (endOfPaginationReached) null else currentPage + 1
                )
            } else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}