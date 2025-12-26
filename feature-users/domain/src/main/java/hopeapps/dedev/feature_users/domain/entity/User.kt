package hopeapps.dedev.feature_users.domain.entity

data class User(
    val login: String,
    val avatarUrl: String,
    val name: String,
    val bio: String,
    val followers: Int,
    val following: Int,
    val publicRepos: Int
)