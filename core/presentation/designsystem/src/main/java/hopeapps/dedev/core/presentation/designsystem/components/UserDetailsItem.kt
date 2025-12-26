package hopeapps.dedev.core.presentation.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.fallback
import coil3.request.placeholder
import hopeapps.dedev.core.presentation.designsystem.R


@Composable
fun UserDetailsItem(
    title: String,
    description: String,
    imageUrl: String,
    @DrawableRes placeholderDrawableRes: Int = R.drawable.ic_test,
    contentDescription: String? = null,
    errorDrawableRes: Int = R.drawable.ic_test,
    onClickItem: () -> Unit
) {

    var isLoading by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
            .clickable {
                onClickItem()
            }
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier.size(52.dp), contentAlignment = Alignment.Center
        ) {

            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .testTag("contact_image"),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .error(errorDrawableRes)
                    .fallback(errorDrawableRes)
                    .placeholder(placeholderDrawableRes)
                    .build(),
                contentDescription = contentDescription,
                onState = { state ->
                    isLoading = state is AsyncImagePainter.State.Loading
                }
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(52.dp)
                        .testTag("image_loader"),
                    color = Color.White,
                    strokeWidth = 3.dp
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            DefaultText(
                modifier = Modifier.padding(top = 8.dp),
                text = title
            )

            DefaultText(
                modifier = Modifier.padding(top = 8.dp),
                text = description
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UserDetailsItemPreview() {
    MaterialTheme {
        UserDetailsItem(
            title = "Título",
            description = "Description",
            imageUrl = "",
            placeholderDrawableRes = R.drawable.ic_test,
            errorDrawableRes = 0,
            onClickItem = {}
        )
    }
}