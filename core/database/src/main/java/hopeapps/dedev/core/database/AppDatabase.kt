package hopeapps.dedev.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import hopeapps.dedev.core.database.dao.RepoDao
import hopeapps.dedev.core.database.dao.RepoRemoteKeyDao
import hopeapps.dedev.core.database.dao.UserDao
import hopeapps.dedev.core.database.model.RepoItemRemoteKeysEntity
import hopeapps.dedev.core.database.model.RepositoryEntity
import hopeapps.dedev.core.database.model.UserEntity

@Database(entities = [UserEntity::class, RepositoryEntity::class, RepoItemRemoteKeysEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun repoDao(): RepoDao
    abstract fun remoteKeyDao(): RepoRemoteKeyDao
}
