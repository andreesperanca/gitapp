package hopeapps.dedev.feature_repo.presentation.search

sealed class RepoSearchAction {
    data class OnSearchClick(val filterText: String): RepoSearchAction()
    data class UpdateSortFilter(val orderByFilter: String): RepoSearchAction()
    data class UpdatedForkFilterType(val isOnlyFork: Boolean): RepoSearchAction()
    data class UpdatedLanguageFilter(val languageFilter: String): RepoSearchAction()
}