package hopeapps.dedev.core.presentation.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultSearchBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onQueryChange: (searchQuery: String) -> Unit,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    filterListener: (searchQuery: String) -> Unit,
    recentSearch: List<String>,
    placeholder: String
) {
    SearchBar(
        modifier = modifier.fillMaxWidth(),
        inputField = {
            SearchBarDefaults.InputField(
                query = searchQuery,
                onQueryChange = { newQuery -> onQueryChange(newQuery) },
                onSearch = {
                    onExpandedChange(false)
                    filterListener(searchQuery)
                },
                expanded = isExpanded,
                onExpandedChange = { isExpanded ->
                    onExpandedChange(isExpanded)
                },
                placeholder = { Text(text = placeholder) },
                leadingIcon = {
                    IconButton(
                        content = {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        onClick = {
                            if (isExpanded) {
                                onExpandedChange(false)
                                filterListener(searchQuery)
                            } else {
                                onExpandedChange(true)
                            }
                        })
                },
                trailingIcon = {
                    if (isExpanded) {
                        IconButton(
                            content = {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            },
                            onClick = {
                                onQueryChange("")
                                onExpandedChange(false)
                            })
                    }
                }
            )
        },
        expanded = isExpanded,
        onExpandedChange = {
            isExpanded
            onExpandedChange(isExpanded)
        },
        shape = SearchBarDefaults.inputFieldShape,
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        shadowElevation = SearchBarDefaults.ShadowElevation,
        windowInsets = SearchBarDefaults.windowInsets,
        content = {
            recentSearch.forEach { item ->
                Text(text = item, fontSize = 15.sp)
            }
        }
    )
}
