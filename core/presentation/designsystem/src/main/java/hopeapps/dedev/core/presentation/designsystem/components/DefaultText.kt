package hopeapps.dedev.core.presentation.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag


@Composable
fun DefaultText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        modifier = modifier.testTag("title_component"),
        text = text,
        color = color,
        style = MaterialTheme.typography.bodyMedium
    )
}
