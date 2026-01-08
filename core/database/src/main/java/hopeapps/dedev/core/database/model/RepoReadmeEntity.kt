package hopeapps.dedev.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "repo_contents")
data class RepoReadmeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val repoId: Long,
    val content: String
)
