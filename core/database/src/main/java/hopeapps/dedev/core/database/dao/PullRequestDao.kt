package hopeapps.dedev.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import hopeapps.dedev.core.database.model.PullRequestEntity

@Dao
interface PullRequestDao {
    @Upsert
    suspend fun upsertAll(prs: List<PullRequestEntity>)

    @Query("SELECT * FROM prs WHERE repoId = :repoId")
    fun getPullRequests(repoId: Long): PagingSource<Int, PullRequestEntity>

    @Query("DELETE FROM prs WHERE repoId = :repoId")
    suspend fun clearAll(repoId: Long)
}