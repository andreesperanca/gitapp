package hopeapps.dedev.feature_repo.data.mapper

import hopeapps.dedev.core.database.model.PullRequestEntity
import hopeapps.dedev.core.network.models.PullRequestDto
import hopeapps.dedev.feature_repo.domain.entity.PullRequest

fun PullRequestEntity.toDomain(): PullRequest {
    return PullRequest(
        id = id,
        repoId = repoId,
        title = title,
        author = author,
        status = status,
        commentsCount = commentsCount
    )
}

fun PullRequestDto.toEntity(repoId: Long): PullRequestEntity {
    return PullRequestEntity(
        id = id,
        repoId = repoId,
        state = state,
        title = title,
        author = user.login,
        status = state,
        commentsCount = 10
    )
}