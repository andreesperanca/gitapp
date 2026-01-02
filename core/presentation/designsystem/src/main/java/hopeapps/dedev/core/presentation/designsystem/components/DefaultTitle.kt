package hopeapps.dedev.core.presentation.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp


@Composable
fun DefaultTitle(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    maxLines: Int  = 1
) {
    Text(
        modifier = modifier.testTag("title_component"),
        text = title,
        color = color,
        fontSize = 16.sp,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}
