package hopeapps.dedev.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hopeapps.dedev.core.database.model.RepoItemRemoteKeysEntity

@Dao
interface RepoRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll (remoteKey: List<RepoItemRemoteKeysEntity>)

    @Query ("SELECT * FROM remote_keys_entity WHERE repoId = :repoId")
    suspend fun remoteKeysByRepoId (repoId: Long): RepoItemRemoteKeysEntity?

    @Query ("DELETE FROM remote_keys_entity")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM remote_keys_entity ORDER BY nextKey DESC LIMIT 1")
    suspend fun getLastRemoteKey(): RepoItemRemoteKeysEntity?

}