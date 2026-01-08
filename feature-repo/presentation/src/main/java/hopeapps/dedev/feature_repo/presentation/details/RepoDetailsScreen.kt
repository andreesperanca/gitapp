package hopeapps.dedev.feature_repo.presentation.details

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import hopeapps.dedev.core.presentation.designsystem.R
import hopeapps.dedev.core.presentation.designsystem.components.DefaultText
import hopeapps.dedev.core.presentation.designsystem.components.DefaultTopAppBar
import hopeapps.dedev.core.presentation.designsystem.screen.LoadingLayout
import hopeapps.dedev.feature_repo.domain.entity.PullRequest
import hopeapps.dedev.feature_repo.presentation.details.content.IssuesContent
import hopeapps.dedev.feature_repo.presentation.details.content.OverviewContent
import hopeapps.dedev.feature_repo.presentation.details.content.PullRequestsContent
import hopeapps.dedev.feature_repo.presentation.details.tab.TabNavItem
import hopeapps.dedev.feature_repo.presentation.utils.IssueUiModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel


@Composable
fun RepoDetailsScreenRoot(
    repoId: Long,
    viewModel: RepoDetailViewModel = koinViewModel(),
    onBackListener: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.repoDetailState.collectAsState()
    val pullRequests = viewModel.pullRequestsPagingFlow.collectAsLazyPagingItems()
    val issues = viewModel.issuesPagingFlow.collectAsLazyPagingItems()

    LaunchedEffect(key1 = repoId) {
        viewModel.start(
            repoId = repoId
        )
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is DetailEvent.OpenRepoInWeb -> {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        event.url.toUri()
                    )
                    context.startActivity(intent)
                }
                is DetailEvent.ShareRepo -> {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, event.url)
                    }

                    context.startActivity(
                        Intent.createChooser(intent, "Compartilhar repositório")
                    )
                }
                DetailEvent.BackListener -> { onBackListener() }
            }
        }
    }

    RepoDetailScreen(
        modifier = Modifier,
        state = state,
        pullRequests = pullRequests,
        issues = issues,
        onAction = viewModel::onAction
    )

}

@Composable
fun RepoDetailScreen(
    modifier: Modifier = Modifier,
    state: RepoDetailState,
    pullRequests: LazyPagingItems<PullRequest>,
    issues: LazyPagingItems<IssueUiModel>,
    onAction: (DetailAction) -> Unit
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
                            IconButton(onClick = { onAction(DetailAction.BackListener) } ) {
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
                                            repoDescription = state.description,
                                            openRepoInWeb = {
                                                onAction(DetailAction.OpenRepoInWeb)
                                            },
                                            shareRepo = {
                                                onAction(DetailAction.ShareRepo)
                                            }
                                        )
                                    }
                                    1 -> { PullRequestsContent(pullRequests = pullRequests) }
                                    2 -> { IssuesContent(issues = issues) }
                                }
                            }
                        }
                    }
                }
            )
        }
    )
}
