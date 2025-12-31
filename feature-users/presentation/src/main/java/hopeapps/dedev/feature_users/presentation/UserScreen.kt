package hopeapps.dedev.feature_users.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hopeapps.dedev.core.presentation.designsystem.components.UserDetailsItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserScreenRoot(
    viewModel: UserViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    UserScreen(
        userState = state,
        onAction = viewModel::onAction
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    userState: UserState,
    onAction: (UserAction) -> Unit
) {

    Scaffold {
        var searchQuery by remember { mutableStateOf("") }
        var active by remember { mutableStateOf(false) }

        //Replicar lógica
//    val filteredItems = items.filter { it.contains(searchQuery, ignoreCase = true) }

        Column(
            modifier = Modifier.padding(it)
        ) {

            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = {
                            active = false
                            onAction(UserAction.FilterClick(userFilterText = searchQuery))
                        },
                        expanded = active,
                        onExpandedChange = { active = it },
                        placeholder = { Text("Search") },
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
                                    if (active) {
                                        active = false
                                        onAction(UserAction.FilterClick(userFilterText = searchQuery))
                                    } else {
                                        active = true
                                    }
                                })

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
                modifier = Modifier
                    .padding(start = 12.dp, top = 2.dp, end = 12.dp, bottom = 12.dp)
                    .fillMaxWidth(),
                shape = SearchBarDefaults.inputFieldShape,
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
                tonalElevation = 0.dp,
                shadowElevation = SearchBarDefaults.ShadowElevation,
                windowInsets = SearchBarDefaults.windowInsets,
                content = {
                    userState.recentUsers.forEach { item ->
                        Text(text = item.login, fontSize = 15.sp)
                    }
                }
            )

            userState.recentUsers.forEach { item ->
                UserDetailsItem(
                    name = item.name,
                    bio = item.bio,
                    follows = item.followers.toString(),
                    following = item.following.toString(),
                    repositories = item.publicRepos.toString(),
                    avatarUrl = item.avatarUrl,
                    contentDescription = item.bio,
                    onClickItem = {
                    }
                )
            }
        }

    }
}


@Composable
@Preview(showBackground = true)
fun UserScreenPreview() {
    UserScreen(
        userState = UserState(),
        onAction = {

        }
    )
}

