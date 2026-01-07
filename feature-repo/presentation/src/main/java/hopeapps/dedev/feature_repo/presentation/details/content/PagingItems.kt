package hopeapps.dedev.feature_repo.presentation.details.content

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems

fun <T : Any> LazyListScope.pagingItems(
    lazyPagingItems: LazyPagingItems<T>,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable (T?) -> Unit
) {
    items(
        count = lazyPagingItems.itemCount,
        key = { index ->
            lazyPagingItems[index]?.let { key?.invoke(it) } ?: "placeholder_index_$index"
        }
    ) { index ->
        itemContent(lazyPagingItems[index])
    }
}