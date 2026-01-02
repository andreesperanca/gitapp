package hopeapps.dedev.gitapp

import android.app.Application
import hopeapps.dedev.core.database.di.databaseModule
import hopeapps.dedev.core.network.di.apiModule
import hopeapps.dedev.core.network.di.networkModule
import hopeapps.dedev.gitapp.di.featureRepo
import hopeapps.dedev.gitapp.di.featureUsers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class GitApp() : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@GitApp)
            modules(
                databaseModule,
                networkModule,
                apiModule,
                featureUsers,
                featureRepo
            )
        }
    }
}