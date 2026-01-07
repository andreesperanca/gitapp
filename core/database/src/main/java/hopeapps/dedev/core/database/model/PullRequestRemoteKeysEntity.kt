package hopeapps.dedev.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "remote_keys_entity_prs"
)
data class PullRequestRemoteKeysEntity(
    @PrimaryKey
    val pullRequestId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)