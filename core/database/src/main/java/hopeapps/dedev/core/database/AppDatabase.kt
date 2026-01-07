package hopeapps.dedev.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hopeapps.dedev.core.database.converter.LabelListConverter
import hopeapps.dedev.core.database.dao.IssueDao
import hopeapps.dedev.core.database.dao.IssueRemoteKeyDao
import hopeapps.dedev.core.database.dao.PullRequestDao
import hopeapps.dedev.core.database.dao.PullRequestRemoteKeyDao
import hopeapps.dedev.core.database.dao.RepoDao
import hopeapps.dedev.core.database.dao.RepoRemoteKeyDao
import hopeapps.dedev.core.database.dao.UserDao
import hopeapps.dedev.core.database.model.IssueEntity
import hopeapps.dedev.core.database.model.IssueRemoteKeysEntity
import hopeapps.dedev.core.database.model.PullRequestEntity
import hopeapps.dedev.core.database.model.PullRequestRemoteKeysEntity
import hopeapps.dedev.core.database.model.RepoItemRemoteKeysEntity
import hopeapps.dedev.core.database.model.RepositoryEntity
import hopeapps.dedev.core.database.model.UserEntity

@Database(entities = [
    UserEntity::class,
    RepositoryEntity::class,
    RepoItemRemoteKeysEntity::class,
    PullRequestEntity::class,
    PullRequestRemoteKeysEntity::class,
    IssueEntity::class,
    IssueRemoteKeysEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LabelListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun repoDao(): RepoDao
    abstract fun remoteKeyDao(): RepoRemoteKeyDao
    abstract fun pullRequestDao(): PullRequestDao
    abstract fun pullRequestRemoteKeyDao(): PullRequestRemoteKeyDao
    abstract fun issueDao(): IssueDao
    abstract fun issueRemoteKeyDao(): IssueRemoteKeyDao
}
