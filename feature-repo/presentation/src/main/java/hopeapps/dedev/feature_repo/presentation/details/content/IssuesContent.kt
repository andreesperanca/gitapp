package hopeapps.dedev.feature_repo.presentation.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import hopeapps.dedev.core.presentation.designsystem.LocalSpacing
import hopeapps.dedev.core.presentation.designsystem.components.EmptyState
import hopeapps.dedev.core.presentation.designsystem.components.ErrorState
import hopeapps.dedev.core.presentation.designsystem.screen.LoadingLayout
import hopeapps.dedev.core.presentation.designsystem.theme.GitappTheme
import hopeapps.dedev.feature_repo.presentation.components.IssueItem
import hopeapps.dedev.feature_repo.presentation.model.IssueUiModel

@Composable
fun IssuesContent(
    issues: LazyPagingItems<IssueUiModel>
) {

    val listState = rememberLazyListState()
    val hasItems = issues.itemCount > 0
    val pagerState = issues.loadState

    LoadingLayout(
        isLoading = pagerState.refresh is LoadState.Loading,
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small),
                    contentPadding = PaddingValues(vertical = LocalSpacing.current.small)
                ) {

                    pagingItems(
                        issues
                    ) { item ->
                        if (item != null) {
                            IssueItem(
                                modifier = Modifier.padding(horizontal = LocalSpacing.current.medium),
                                issue = item
                            )
                        }
                    }
                }

                if (issues.loadState.refresh is LoadState.Loading && !hasItems) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                if (issues.loadState.refresh is LoadState.Error && !hasItems) {
                    ErrorState(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = LocalSpacing.current.large),
                        message = stringResource(
                            hopeapps.dedev.feature_repo.presentation.R.string.without_connection
                        )
                    )
                }

                if (issues.loadState.refresh is LoadState.NotLoading && !hasItems) {
                    EmptyState(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = LocalSpacing.current.large),
                        message = stringResource(
                            hopeapps.dedev.feature_repo.presentation.R.string.nothing_here
                        ),
                        description = stringResource(
                            hopeapps.dedev.feature_repo.presentation.R.string.nothing_here_description
                        )
                    )
                }
            }
        }
    )
}
