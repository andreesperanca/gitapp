package hopeapps.dedev.feature_repo.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import hopeapps.dedev.feature_repo.domain.entity.RepoSort
import hopeapps.dedev.feature_repo.presentation.R

@Composable
fun RepoSort.mapToLabel(): String = when (this) {
    RepoSort.Stars -> stringResource(R.string.stars)
    RepoSort.Forks -> stringResource(R.string.forks)
    RepoSort.Updated -> stringResource(R.string.last_update)
}
