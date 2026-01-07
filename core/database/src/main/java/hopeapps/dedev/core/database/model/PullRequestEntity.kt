package hopeapps.dedev.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "prs"
)
data class PullRequestEntity(
    @PrimaryKey
    val id: Long,
    val repoId: Long,
    val state: String,
    val title: String,
    val author: String,
    val status: String,
    val commentsCount: Int
)