package hopeapps.dedev.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import hopeapps.dedev.core.database.model.RepositoryEntity

@Dao
interface RepoDao {
    @Upsert
    suspend fun upsertAll(repos: List<RepositoryEntity>)

    @Query("SELECT * FROM repositories")
    fun getAllRepositories(): PagingSource<Int, RepositoryEntity>

    @Query("DELETE FROM repositories")
    suspend fun clearAll()
}