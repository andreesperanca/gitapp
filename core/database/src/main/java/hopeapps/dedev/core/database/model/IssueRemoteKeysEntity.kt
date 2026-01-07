package hopeapps.dedev.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "remote_keys_entity_issues"
)
data class IssueRemoteKeysEntity(
    @PrimaryKey
    val issueId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)