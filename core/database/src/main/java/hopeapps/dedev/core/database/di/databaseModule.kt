package hopeapps.dedev.core.database.di

import androidx.room.Room
import hopeapps.dedev.core.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val databaseModule = module {
    single {
        Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                "database"
            ).fallbackToDestructiveMigration(false)
            .build()
    }

    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().repoDao() }
    single { get<AppDatabase>().repoLanguageDao() }
    single { get<AppDatabase>().repoContentDao() }
}