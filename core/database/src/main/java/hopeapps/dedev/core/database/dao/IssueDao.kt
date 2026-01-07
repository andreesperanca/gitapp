package hopeapps.dedev.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import hopeapps.dedev.core.database.model.IssueEntity

@Dao
interface IssueDao {
    @Upsert
    suspend fun upsertAll(issues: List<IssueEntity>)

    @Query("SELECT * FROM issues WHERE repoId = :repoId")
    fun getIssues(repoId: Long): PagingSource<Int, IssueEntity>

    @Query("DELETE FROM issues WHERE repoId = :repoId")
    suspend fun clearAll(repoId: Long)
}