package hopeapps.dedev.core.network.di

import hopeapps.dedev.core.network.GitApi
import org.koin.dsl.module
import retrofit2.Retrofit


val apiModule = module {
    single {
        get<Retrofit>()
            .create(GitApi::class.java)
    }
}