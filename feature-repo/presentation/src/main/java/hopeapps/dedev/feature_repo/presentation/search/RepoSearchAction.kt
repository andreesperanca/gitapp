package hopeapps.dedev.feature_repo.presentation.search

import hopeapps.dedev.feature_repo.domain.entity.RepoSort
import hopeapps.dedev.feature_repo.domain.entity.Repository

sealed class RepoSearchAction {
    data class OnSearchClick(val filterText: String): RepoSearchAction()
    data class OnRepositorySelected(val repository: Repository): RepoSearchAction()
    data class UpdateSortFilter(val orderByFilter: RepoSort): RepoSearchAction()
    data class UpdatedForkFilterType(val isOnlyFork: Boolean): RepoSearchAction()
    data class UpdatedLanguageFilter(val languageFilter: String): RepoSearchAction()
}