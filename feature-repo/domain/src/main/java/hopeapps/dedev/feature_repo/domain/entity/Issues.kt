package hopeapps.dedev.feature_repo.domain.entity

data class Issue(
    val id: Long,
    val repoId: Long,
    val title: String,
    val author: String,
    val state: String,
    val labels: List<Label>,
    val createdAt: String,
)