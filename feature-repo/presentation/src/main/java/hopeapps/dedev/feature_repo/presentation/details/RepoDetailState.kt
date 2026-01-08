package hopeapps.dedev.feature_repo.presentation.details

data class RepoDetailState (
    val stars: Int = 0,
    val forks: Int = 0,
    val watchers: Int = 0,
    val issues: Int = 0,
    val name: String = "",
    val description: String = "",
    val languages: String = "",
    val readme: String = "",
    val isLoading: Boolean = false,
    val urlRepo: String = ""
)