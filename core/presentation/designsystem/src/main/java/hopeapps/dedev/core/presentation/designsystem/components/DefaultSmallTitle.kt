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
fun DefaultSmallTitle(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        modifier = modifier.testTag("title_component"),
        text = title,
        color = color,
        fontSize = 14.sp,
        overflow = TextOverflow.Ellipsis
    )
}
