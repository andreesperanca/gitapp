package hopeapps.dedev.feature_repo.data.mapper

import hopeapps.dedev.core.database.model.RepositoryEntity
import hopeapps.dedev.core.network.models.RepositoryDto
import hopeapps.dedev.feature_repo.domain.entity.RepoSort
import hopeapps.dedev.feature_repo.domain.entity.Repository

fun RepositoryDto.toDomain(): Repository {
    return Repository(
        id = id,
        name = name,
        description = description ?: "",
        stars = stargazersCount,
        forks = forks,
        language = language ?: "",
        lastUpdate = updatedAt,
        isFork = isFork,
    )
}


fun RepositoryDto.toRepositoryEntity(): RepositoryEntity {
    return RepositoryEntity(
        id = id,
        name = name,
        description = description ?: "",
        stars = stargazersCount,
        forks = forks,
        language = language ?: "",
        lastUpdate = updatedAt,
        isFork = isFork,
        userLogin = owner.login
    )
}


fun List<RepositoryDto>.toDomain(): List<Repository> {
    return map { it.toDomain() }
}

fun RepositoryEntity.toDomain(): Repository {
    return Repository(
        id = id,
        name = name,
        description = description,
        stars = stars,
        forks = forks,
        language = language,
        lastUpdate = lastUpdate,
        isFork = isFork,
    )
}


fun RepoSort.toApiValue(): String =
    when (this) {
        RepoSort.Stars -> "stars"
        RepoSort.Forks -> "forks"
        RepoSort.Updated -> "updated"
    }
