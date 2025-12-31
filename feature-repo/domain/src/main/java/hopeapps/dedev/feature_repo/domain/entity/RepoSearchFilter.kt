package hopeapps.dedev.feature_repo.domain.entity

data class RepoSearchFilter(
    val user: String? = null,
    val language: String = "",
    val forkFilter: ForkFilterType = ForkFilterType.All,
    val sort: RepoSort = RepoSort.Stars
) {
    fun buildQuery(): String {
        val qualifiers = mutableListOf<String>()

        user?.takeIf { it.isNotBlank() }?.let {
            qualifiers += "user:$it"
        }

        language.takeIf { it.isNotBlank() }?.let {
            qualifiers += "language:$it"
        }

        qualifiers += when (forkFilter) {
            ForkFilterType.OnlyForks -> "fork:only"
            ForkFilterType.All -> "fork:false"
        }

        return qualifiers.joinToString(" ")
    }
}

sealed interface ForkFilterType {
    object OnlyForks : ForkFilterType
    object All : ForkFilterType
}


sealed interface RepoSort {
    object Stars : RepoSort
    object Forks : RepoSort
    object Updated : RepoSort
}
