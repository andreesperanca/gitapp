package hopeapps.dedev.feature_repo.data.mapper

import hopeapps.dedev.core.network.models.SearchResponseDto
import hopeapps.dedev.feature_repo.domain.entity.SearchResponse


fun SearchResponseDto.toDomain(): SearchResponse {
    return SearchResponse(
        totalCount = totalCount,
        items = items.toDomain()
    )
}

fun List<SearchResponseDto>.toDomain(): List<SearchResponse> {
    return map { it.toDomain() }
}