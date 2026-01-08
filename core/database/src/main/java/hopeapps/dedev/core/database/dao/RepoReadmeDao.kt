package hopeapps.dedev.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hopeapps.dedev.core.database.model.RepoReadmeEntity

@Dao
interface RepoReadmeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repoContentEntity: RepoReadmeEntity)

    @Query("SELECT * FROM repo_contents WHERE repoId = :repoId")
    suspend fun getByRepoId(repoId: Long): RepoReadmeEntity?

    @Query("DELETE FROM repo_contents")
    suspend fun clear()
}
