package hopeapps.dedev.feature_repo.data.mapper

import hopeapps.dedev.core.database.model.RepoReadmeEntity
import hopeapps.dedev.core.database.model.RepoLanguageEntity
import hopeapps.dedev.core.database.model.RepositoryEntity
import hopeapps.dedev.core.network.models.LanguagesDto
import hopeapps.dedev.core.network.models.RepoReadmeDto
import hopeapps.dedev.core.network.models.RepositoryDto
import hopeapps.dedev.feature_repo.domain.entity.RepoReadme
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
        repoOwner = owner.login,
        watchers = watchersCount,
        issues = openIssuesCount
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
        repoOwner = owner.login,
        watchers = watchers,
        issues = openIssuesCount
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
        repoOwner = repoOwner,
        watchers = watchers,
        issues = issues
    )
}


fun RepoReadmeDto.toDomain(): RepoReadme {
    return RepoReadme(
        content = content
    )
}


fun RepoReadmeDto.toEntity(repoId: Long): RepoReadmeEntity {
    return RepoReadmeEntity(
        repoId = repoId,
        content = content
    )
}


fun RepoSort.toApiValue(): String =
    when (this) {
        RepoSort.Stars -> "stars"
        RepoSort.Forks -> "forks"
        RepoSort.Updated -> "updated"
    }

fun LanguagesDto.toDomain() : List<String> {
    return map { languages ->
        languages.key
    }
}

fun LanguagesDto.toEntity(repoId: Long) : RepoLanguageEntity {
    return RepoLanguageEntity(
        repoId = repoId,
        languages = map { languages ->
            languages.key
        }
    )
}


fun RepoReadmeEntity.toDomain(): RepoReadme {
    return RepoReadme(
        content = content
    )
}

fun RepoLanguageEntity.toDomain(): List<String> {
    return languages
}
