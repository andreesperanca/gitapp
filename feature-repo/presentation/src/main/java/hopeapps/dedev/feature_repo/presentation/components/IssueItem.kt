package hopeapps.dedev.feature_repo.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hopeapps.dedev.core.presentation.designsystem.LocalSpacing
import hopeapps.dedev.core.presentation.designsystem.components.DefaultText
import hopeapps.dedev.core.presentation.designsystem.theme.GitappTheme
import hopeapps.dedev.feature_repo.domain.entity.Label
import hopeapps.dedev.feature_repo.presentation.model.IssueUiModel


@Composable
fun IssueItem(
    modifier: Modifier = Modifier,
    issue: IssueUiModel
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(LocalSpacing.current.small),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .padding(LocalSpacing.current.small),
            verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small)
        ) {

            DefaultText(
                text = issue.title,
                maxLines = 2
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.small)
            ) {

                DefaultText(
                    text = issue.author,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                DefaultText(
                    text = issue.createdAtFormatted,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (issue.labels.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.small),
                    verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.extraSmall)
                ) {
                    issue.labels.forEach { label ->
                        LabelChip(label)
                    }
                }
            }
        }
    }
}

@Composable
fun LabelChip(
    label: Label
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color(label.colorHex).copy(alpha = 0.15f)
    ) {
        DefaultText(
            text = label.name,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            color = Color(label.colorHex)
        )
    }
}


@Preview
@Composable
fun IssueContentPreview() {
    GitappTheme {
        IssueItem(
            modifier = Modifier,
            issue = IssueUiModel(
                id = 1,
                title = "Issue title",
                author = "Author",
                createdAtFormatted = "",
                labels = listOf(Label(name = "Bug", colorHex = 0xFF0000, id = 1))
            )
        )
    }
}
