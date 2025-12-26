package hopeapps.dedev.core.presentation.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag


@Composable
fun DefaultTitle(
    modifier: Modifier = Modifier,
    title: String,
    color: Color
) {
    Text(
        modifier = modifier.testTag("title_component"),
        text = title,
        color = color,
        style = MaterialTheme.typography.titleMedium
    )
}
