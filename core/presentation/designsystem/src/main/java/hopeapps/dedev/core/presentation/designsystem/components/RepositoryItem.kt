package hopeapps.dedev.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hopeapps.dedev.core.presentation.designsystem.LocalSpacing

@Composable
fun RepositoryItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    name: String,
    description: String?,
    language: String?,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(LocalSpacing.current.small)
        ) {

            DefaultTitle(
                title = name,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (!description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(LocalSpacing.current.extraSmall))
                DefaultText(
                    text = description,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.extraSmall))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!language.isNullOrBlank()) {
                    RepoInfoItem(
                        text = language
                    )
                }
            }
        }
    }
}

@Composable
private fun RepoInfoItem(
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DotIndicator()

        DefaultText(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
