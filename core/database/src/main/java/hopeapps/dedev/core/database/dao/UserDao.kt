package hopeapps.dedev.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hopeapps.dedev.core.database.model.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    suspend fun fetchUsers(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecentUsers(user: UserEntity)

}