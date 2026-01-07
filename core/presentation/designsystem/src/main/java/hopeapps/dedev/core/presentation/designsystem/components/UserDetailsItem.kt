package hopeapps.dedev.core.presentation.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.fallback
import coil3.request.placeholder
import hopeapps.dedev.core.presentation.designsystem.LocalSpacing
import hopeapps.dedev.core.presentation.designsystem.R


@Composable
fun UserDetailsItem(
    modifier: Modifier = Modifier,
    name: String,
    bio: String,
    follows: String,
    following: String,
    repositories: String,
    avatarUrl: String,
    @DrawableRes placeholderDrawableRes: Int = R.drawable.ic_test,
    contentDescription: String? = null,
    errorDrawableRes: Int = R.drawable.ic_test,
    onClickItem: () -> Unit
) {

    var isLoading by rememberSaveable { mutableStateOf(true) }
    var isMoreInfoVisible by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        onClick = {
            onClickItem()
        }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = LocalSpacing.current.small,
                        vertical = LocalSpacing.current.extraSmall
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier.size(52.dp),
                    contentAlignment = Alignment.Center
                ) {

                    if (LocalInspectionMode.current) {
                        Image(
                            modifier = Modifier.clip(CircleShape),
                            painter = painterResource(R.drawable.ic_test),
                            contentDescription = contentDescription
                        )
                    } else {
                        AsyncImage(
                            modifier = Modifier.clip(CircleShape),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(avatarUrl)
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
                    }

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(LocalSpacing.current.small))

                Column(
                    modifier = Modifier
                        .animateContentSize()
                        .heightIn(
                            max = if (isMoreInfoVisible) Dp.Unspecified else 52.dp
                        )
                        .weight(1f)
                ) {
                    DefaultTitle(
                        title = name,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )

                    DefaultText(
                        text = bio,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }

                IconButton(onClick = { isMoreInfoVisible = !isMoreInfoVisible }) {
                    Icon(
                        imageVector = if (isMoreInfoVisible) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(isMoreInfoVisible) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoItem(
                        title = stringResource(R.string.follows),
                        value = follows
                    )
                    InfoItem(
                        title = stringResource(R.string.following),
                        value = following
                    )
                    InfoItem(
                        title = stringResource(R.string.repositories),
                        value = repositories
                    )
                }
            }
        }
    }

}


@Composable
fun InfoItem(modifier: Modifier = Modifier, title: String, value: String) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultText(text = title)
        DefaultText(text = value)
    }
}

@Preview(showBackground = true)
@Composable
fun InfoItemPreview() {
    MaterialTheme {
        InfoItem(title = "Title", value = "Value")
    }
}


@Preview(showBackground = true)
@Composable
fun UserDetailsItemPreview() {
    MaterialTheme {
        UserDetailsItem(
            placeholderDrawableRes = R.drawable.ic_test,
            errorDrawableRes = 0,
            onClickItem = {},
            name = "André Esperança",
            bio = "Desenvolvedor Android desde 2023",
            follows = "",
            following = "",
            repositories = "",
            avatarUrl = "",
            contentDescription = ""
        )
    }
}