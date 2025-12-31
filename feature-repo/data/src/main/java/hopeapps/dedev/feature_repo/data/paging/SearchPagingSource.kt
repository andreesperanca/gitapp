package hopeapps.dedev.feature_repo.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import hopeapps.dedev.core.network.GitApi
import hopeapps.dedev.feature_repo.data.mapper.toApiValue
import hopeapps.dedev.feature_repo.data.mapper.toDomain
import hopeapps.dedev.feature_repo.domain.entity.RepoSearchFilter
import hopeapps.dedev.feature_repo.domain.entity.Repository


class SearchPagingSource(
    private val gitApi: GitApi,
    private val filter: RepoSearchFilter
) : PagingSource<Int, Repository>() {

    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        val currentPage = params.key ?: 1
        return try {

            //Let the comment, is necessary toDomain here?
            //OR in repository layer?
            val response = gitApi.searchRepositories(
                query = filter.buildQuery(),
                sort = filter.sort.toApiValue(),
                page = currentPage,
                perPage = params.loadSize
            ).toDomain()

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