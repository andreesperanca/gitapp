package hopeapps.dedev.feature_repo.presentation.search

import hopeapps.dedev.feature_repo.domain.entity.RepoSearchFilter

data class RepoSearchState(
    val filter: RepoSearchFilter = RepoSearchFilter()
)
