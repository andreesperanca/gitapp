package hopeapps.dedev.feature_repo.domain.entity

data class SearchResponse(
    val totalCount: Int,
    val items: List<Repository>
)
