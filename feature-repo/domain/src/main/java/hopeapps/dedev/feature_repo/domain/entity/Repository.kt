package hopeapps.dedev.feature_repo.domain.entity

data class Repository(
    val id: Long,
    val name: String,
    val description: String,
    val stars: Int,
    val forks: Int,
    val language: String,
    val lastUpdate: String,
    val isFork: Boolean,
)
