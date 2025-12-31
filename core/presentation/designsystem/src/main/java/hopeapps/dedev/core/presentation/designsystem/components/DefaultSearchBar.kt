package hopeapps.dedev.core.presentation.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hopeapps.dedev.core.presentation.designsystem.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultSearchBar(
    modifier: Modifier = Modifier,
    placeHolder: String,
    recentSearchList: List<String> = emptyList(),
    onSearch: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    onSettingClick: () -> Unit
) {

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        modifier = modifier
            .padding(
                horizontal = LocalSpacing.current.medium,
                vertical = LocalSpacing.current.small
            )
            .fillMaxWidth(),
        inputField = {
            SearchBarDefaults.InputField(
                query = searchQuery,
                onQueryChange = { newQuery ->
                    searchQuery = newQuery
                    onQueryChange(newQuery)
                },
                onSearch = {
                    active = false
                    onSearch(searchQuery)
                },
                expanded = active,
                onExpandedChange = { active = it },
                placeholder = { Text(placeHolder) },
                leadingIcon = {
                    IconButton(
                        content = {
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        onClick = { onSettingClick() })
                },
                trailingIcon = {
                    if (active) {
                        IconButton(
                            content = {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            },
                            onClick = {
                                searchQuery = ""
                                active = false
                            })
                    }
                }
            )
        },
        expanded = active,
        onExpandedChange = { active = it },
        shape = SearchBarDefaults.inputFieldShape,
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        tonalElevation = 0.dp,
        shadowElevation = SearchBarDefaults.ShadowElevation,
        windowInsets = SearchBarDefaults.windowInsets,
        content = {
            recentSearchList.forEach { item ->
                Text(text = item, fontSize = 15.sp)
            }
        }
    )
}
