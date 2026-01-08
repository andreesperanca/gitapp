package hopeapps.dedev.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hopeapps.dedev.core.database.model.RepoLanguageEntity

@Dao
interface RepoLanguageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repoLanguageEntity: RepoLanguageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<RepoLanguageEntity>)

    @Query("SELECT * FROM repo_languages WHERE repoId = :repoId")
    suspend fun getByRepoId(repoId: Long): RepoLanguageEntity?

    @Query("DELETE FROM repo_languages")
    suspend fun clear()
}