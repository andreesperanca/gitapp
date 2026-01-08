package hopeapps.dedev.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "repo_languages")
data class RepoLanguageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val repoId: Long,
    val languages: List<String>
)
