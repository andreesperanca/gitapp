package hopeapps.dedev.feature_repo.presentation.details

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon

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