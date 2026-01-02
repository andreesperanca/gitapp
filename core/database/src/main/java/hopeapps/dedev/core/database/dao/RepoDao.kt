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

    @Query("SELECT * FROM repositories WHERE userLogin = :userFilterText")
    fun getAllRepositories(userFilterText: String): PagingSource<Int, RepositoryEntity>

    @Query("DELETE FROM repositories WHERE userLogin = :userLogin")
    suspend fun clearAll(userLogin: String)
}