package hopeapps.dedev.feature_repo.presentation.details.tab

import androidx.annotation.StringRes
import hopeapps.dedev.feature_repo.presentation.R

sealed class TabNavItem(
    val type: TabType,
    @param:StringRes val title: Int
) {
    companion object {
        val items = listOf(
            Overview,
            PRS,
            Issues
        )
    }

    data object Overview: TabNavItem(TabType.OVERVIEW, R.string.overview)
    data object PRS: TabNavItem(TabType.PULL_REQUESTS, R.string.pull_requests)
    data object Issues: TabNavItem(TabType.ISSUES, R.string.issues)
}

enum class TabType {
    ISSUES,
    PULL_REQUESTS,
    OVERVIEW
}