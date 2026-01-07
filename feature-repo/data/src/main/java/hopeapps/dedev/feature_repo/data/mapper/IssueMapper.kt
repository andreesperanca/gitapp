package hopeapps.dedev.feature_repo.data.mapper

import hopeapps.dedev.core.database.model.IssueEntity
import hopeapps.dedev.core.database.model.LabelEntity
import hopeapps.dedev.core.network.models.IssueDto
import hopeapps.dedev.core.network.models.LabelDto
import hopeapps.dedev.feature_repo.domain.entity.Issue
import hopeapps.dedev.feature_repo.domain.entity.Label


fun LabelEntity.toDomain(): Label {
    return Label(
        id = id,
        name = name,
        colorHex = colorHex
    )
}

fun LabelDto.toEntity(): LabelEntity {
    return LabelEntity(
        id = id,
        name = name,
        colorHex = colorHex
    )
}




fun IssueEntity.toDomain(): Issue {
    return Issue(
        id = id,
        repoId = repoId,
        title = title,
        author = author,
        state = state,
        labels = labels.map { label -> label.toDomain() },
        createdAt = createdAt
    )
}

fun IssueDto.toEntity(repoId: Long): IssueEntity {
    return IssueEntity(
        id = id,
        repoId = repoId,
        title = title,
        author = user.login,
        state = state,
        labels = labels.map { label -> label.toEntity() },
        createdAt = createdAt
    )
}