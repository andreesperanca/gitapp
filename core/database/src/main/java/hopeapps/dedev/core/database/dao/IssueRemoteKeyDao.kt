package hopeapps.dedev.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hopeapps.dedev.core.database.model.IssueRemoteKeysEntity

@Dao
interface IssueRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll (remoteKey: List<IssueRemoteKeysEntity>)

    @Query ("SELECT * FROM remote_keys_entity_issues WHERE issueId = :issueId")
    suspend fun remoteKeysByIssuesId (issueId: Long): IssueRemoteKeysEntity?

    @Query ("DELETE FROM remote_keys_entity_issues")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM remote_keys_entity_issues ORDER BY nextKey DESC LIMIT 1")
    suspend fun getLastRemoteKey(): IssueRemoteKeysEntity?

}