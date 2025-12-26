package hopeapps.dedev.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import hopeapps.dedev.core.database.dao.UserDao
import hopeapps.dedev.core.database.model.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
