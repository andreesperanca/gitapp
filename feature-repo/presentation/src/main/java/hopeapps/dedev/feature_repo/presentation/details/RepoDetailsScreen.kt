package hopeapps.dedev.feature_repo.presentation.details

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import hopeapps.dedev.core.presentation.designsystem.R
import hopeapps.dedev.core.presentation.designsystem.components.DefaultText
import hopeapps.dedev.core.presentation.designsystem.components.DefaultTopAppBar
import hopeapps.dedev.core.presentation.designsystem.screen.LoadingLayout
import hopeapps.dedev.core.presentation.designsystem.theme.GitappTheme
import hopeapps.dedev.feature_repo.domain.entity.Issue
import hopeapps.dedev.feature_repo.domain.entity.PullRequest
import hopeapps.dedev.feature_repo.presentation.details.content.IssuesContent
import hopeapps.dedev.feature_repo.presentation.details.content.OverviewContent
import hopeapps.dedev.feature_repo.presentation.details.content.PullRequestsContent
import hopeapps.dedev.feature_repo.presentation.details.tab.TabNavItem
import hopeapps.dedev.feature_repo.presentation.utils.IssueUiModel
import io.noties.markwon.Markwon
import org.koin.androidx.compose.koinViewModel


@Composable
fun RepoDetailsScreenRoot(
    repoId: Long,
    viewModel: RepoDetailViewModel = koinViewModel()
) {
    val state by viewModel.repoDetailState.collectAsState()
    val pullRequests = viewModel.pullRequestsPagingFlow.collectAsLazyPagingItems()
    val issues = viewModel.issuesPagingFlow.collectAsLazyPagingItems()


    LaunchedEffect(key1 = repoId) {
        viewModel.start(
            repoId = repoId
        )
    }

    RepoDetailScreen(
        modifier = Modifier,
        state = state,
        pullRequests = pullRequests,
        issues = issues
    )

}

@Composable
fun RepoDetailScreen(
    modifier: Modifier = Modifier,
    state: RepoDetailState,
    pullRequests: LazyPagingItems<PullRequest>,
    issues: LazyPagingItems<IssueUiModel>
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { TabNavItem.items.size }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }

    LoadingLayout(
        isLoading = state.isLoading,
        content = {
            Scaffold(
                topBar = {
                    DefaultTopAppBar(
                        title = stringResource(R.string.repository_details),
                        navigationIcon = {
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    )
                },
                content = { paddingValues ->

                    Column(
                        modifier = modifier.padding(paddingValues)
                    ) {

                        TabRow(
                            selectedTabIndex = selectedTabIndex,
                            tabs = {
                                TabNavItem.items.forEachIndexed { index, tab ->
                                    Tab(
                                        selected = selectedTabIndex == index,
                                        onClick = { selectedTabIndex = index },
                                        text = {
                                            DefaultText(
                                                text = stringResource(tab.title),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    )
                                }
                            }
                        )

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)

                        ) { index ->
                            Box {
                                when (index) {
                                    0 -> {
                                        OverviewContent(
                                            stars = state.stars,
                                            readMe = state.readme,
                                            languages = state.languages,
                                            forks = state.forks,
                                            watchers = state.watchers,
                                            issues = state.issues,
                                            repoName = state.name,
                                            repoDescription = state.description
                                        )
                                    }

                                    1 -> {
                                        PullRequestsContent(pullRequests = pullRequests)
                                    }

                                    2 -> {
                                        IssuesContent(issues = issues)
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    )
}


@Composable
fun MarkdownComponent(
    modifier: Modifier = Modifier,
    markdown: String
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = { textView ->
            val markwon = Markwon.create(textView.context)
            markwon.setMarkdown(textView, markdown)
        }
    )
}


@Preview
@Composable
private fun RepoDetailsScreenPreview() {
    GitappTheme {
//        RepoDetailScreen(
//            state = RepoDetailState(),
//
//        )
    }
}