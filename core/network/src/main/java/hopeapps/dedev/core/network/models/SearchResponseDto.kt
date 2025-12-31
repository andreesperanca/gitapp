package hopeapps.dedev.core.network.models

import com.google.gson.annotations.SerializedName

data class SearchResponseDto(
    @SerializedName("total_count")
    val totalCount: Int,
    val items: List<RepositoryDto>
)
