package hopeapps.dedev.feature_repo.domain.entity

data class PullRequest(
    val id: Long,
    val repoId: Long,
    val title: String,
    val author: String,
    val status: String,
    val commentsCount: Int
)