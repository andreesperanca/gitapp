package hopeapps.dedev.core.network.di

import hopeapps.dedev.core.network.UserApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit


val apiModule = module {
    single {
        get<Retrofit>(named("USER")).create(UserApi::class.java)
    }
}