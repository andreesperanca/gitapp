package hopeapps.dedev.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "issues"
)
data class IssueEntity(
    @PrimaryKey
    val id: Long,
    val repoId: Long,
    val title: String,
    val author: String,
    val state: String,
    val createdAt: String,
    val labels: List<LabelEntity>
)