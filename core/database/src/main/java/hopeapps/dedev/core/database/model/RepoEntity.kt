package hopeapps.dedev.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "repositories"
)
data class RepositoryEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val description: String,
    val stars: Int,
    val forks: Int,
    val language: String,
    val lastUpdate: String,
    val isFork: Boolean,
)