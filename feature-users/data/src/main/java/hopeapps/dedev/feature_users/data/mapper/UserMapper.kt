package hopeapps.dedev.feature_users.data.mapper

import hopeapps.dedev.core.database.model.UserEntity
import hopeapps.dedev.core.network.models.UserDto
import hopeapps.dedev.feature_users.domain.entity.User

fun UserDto.toUser(): User {
    return User(
        login = login,
        avatarUrl = avatarUrl,
        name = name ?: "",
        followers = followers,
        following = following,
        bio = bio ?: "",
        publicRepos = publicRepos
    )
}

fun List<UserDto>.dtoToUsers(): List<User> {
    return map { it.toUser() }
}


fun UserEntity.toUser(): User {
    return User(
        login = login,
        avatarUrl = avatarUrl,
        name = name,
        followers = followers,
        following = following,
        bio = bio,
        publicRepos = publicRepos
    )
}

fun List<UserEntity>.entityToUsers(): List<User> {
    return map { it.toUser() }
}

