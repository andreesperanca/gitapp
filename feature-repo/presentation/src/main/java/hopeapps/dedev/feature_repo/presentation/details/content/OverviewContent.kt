package hopeapps.dedev.feature_repo.presentation.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hopeapps.dedev.core.presentation.designsystem.LocalSpacing
import hopeapps.dedev.core.presentation.designsystem.components.DefaultSmallTitle
import hopeapps.dedev.core.presentation.designsystem.components.DefaultText
import hopeapps.dedev.core.presentation.designsystem.components.DefaultTitle
import hopeapps.dedev.core.presentation.designsystem.components.InfoItem
import hopeapps.dedev.core.presentation.designsystem.theme.GitappTheme
import hopeapps.dedev.feature_repo.presentation.details.MarkdownComponent

@Composable
fun OverviewContent(
    modifier: Modifier = Modifier,
    stars: Int,
    forks: Int,
    watchers: Int,
    issues: Int,
    readMe: String,
    languages: String,
    repoName: String,
    repoDescription: String,
    openRepoInWeb: () -> Unit,
    shareRepo: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = LocalSpacing.current.medium,
                end = LocalSpacing.current.medium
            )
    ) {

        DefaultTitle(
            modifier = Modifier.padding(top = LocalSpacing.current.small),
            title = repoName
        )

        if (repoDescription.isNotEmpty()) {
            DefaultText(
                modifier = Modifier,
                text = repoDescription
            )
        }

        Row(
            modifier = Modifier.padding(top = LocalSpacing.current.small),
            verticalAlignment = Alignment.CenterVertically
        )
        {

            DefaultSmallTitle(
                modifier = Modifier.weight(1f),
                title = stringResource(hopeapps.dedev.feature_repo.presentation.R.string.statistics)
            )

            IconButton (
                content = {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                onClick = {  shareRepo()  }
            )

            IconButton (
                content = {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                onClick = {  openRepoInWeb()  }
            )
        }

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = LocalSpacing.current.small
                )
        )
        {

            InfoItem(
                modifier = Modifier.padding(PaddingValues(0.dp)),
                title = stringResource(hopeapps.dedev.feature_repo.presentation.R.string.stars),
                value = stars.toString()
            )
            InfoItem(
                modifier = Modifier.padding(PaddingValues(0.dp)),
                title = stringResource(hopeapps.dedev.feature_repo.presentation.R.string.forks),
                value = forks.toString()
            )
            InfoItem(
                modifier = Modifier.padding(PaddingValues(0.dp)),
                title = stringResource(hopeapps.dedev.feature_repo.presentation.R.string.watchers),
                value = watchers.toString()
            )
            InfoItem(
                modifier = Modifier.padding(PaddingValues(0.dp)),
                title = stringResource(hopeapps.dedev.feature_repo.presentation.R.string.issues),
                value = issues.toString()
            )
        }

        if (languages.isNotEmpty()) {
            DefaultSmallTitle(
                modifier = Modifier
                    .padding(top = 16.dp),
                title = stringResource(hopeapps.dedev.feature_repo.presentation.R.string.languages)
            )

            DefaultText(
                modifier = Modifier,
                text = languages,
                maxLines = 4
            )
        }

        if (readMe.isNotEmpty()) {
            DefaultSmallTitle(
                modifier = Modifier
                    .padding(top = 16.dp),
                title = stringResource(hopeapps.dedev.feature_repo.presentation.R.string.read_me)
            )

            MarkdownComponent (
                modifier = Modifier.padding(top = 16.dp),
                markdown = readMe
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun OverviewPreview() {
    GitappTheme {
        OverviewContent(
            stars = 10,
            readMe = "",
            forks = 1,
            issues = 1,
            watchers = 1,
            languages = "Kotlin, Java ",
            repoName = "Repo Name",
            repoDescription = "Repo Description",
            openRepoInWeb = {},
            shareRepo = {}
        )
    }
}
