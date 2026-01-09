package hopeapps.dedev.feature_repo.presentation.model

import hopeapps.dedev.feature_repo.domain.entity.Label

data class IssueUiModel(
    val id: Long,
    val title: String,
    val author: String,
    val createdAtFormatted: String,
    val labels: List<Label>
)