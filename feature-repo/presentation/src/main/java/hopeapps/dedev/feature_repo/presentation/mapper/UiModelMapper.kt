package hopeapps.dedev.feature_repo.presentation.mapper

import hopeapps.dedev.feature_repo.domain.entity.Issue
import hopeapps.dedev.feature_repo.presentation.model.IssueUiModel
import hopeapps.dedev.feature_repo.presentation.utils.formatDate


fun Issue.toUiModel(): IssueUiModel {
    return IssueUiModel(
        id = id,
        title = title,
        author = author,
        createdAtFormatted = formatDate(createdAt),
        labels = labels
    )
}