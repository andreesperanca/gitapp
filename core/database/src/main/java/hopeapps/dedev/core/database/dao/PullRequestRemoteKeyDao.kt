package hopeapps.dedev.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hopeapps.dedev.core.database.model.PullRequestRemoteKeysEntity
import hopeapps.dedev.core.database.model.RepoItemRemoteKeysEntity

@Dao
interface PullRequestRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll (remoteKey: List<PullRequestRemoteKeysEntity>)

    @Query ("SELECT * FROM remote_keys_entity_prs WHERE pullRequestId = :prId")
    suspend fun remoteKeysByPullRequestId (prId: Long): PullRequestRemoteKeysEntity?

    @Query ("DELETE FROM remote_keys_entity_prs")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM remote_keys_entity_prs ORDER BY nextKey DESC LIMIT 1")
    suspend fun getLastRemoteKey(): PullRequestRemoteKeysEntity?

}