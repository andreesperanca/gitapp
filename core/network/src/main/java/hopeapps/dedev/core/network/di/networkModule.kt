package hopeapps.dedev.core.network.di

import com.google.gson.GsonBuilder
import hopeapps.dedev.core.network.AuthInterceptor
import hopeapps.dedev.core.network.BuildConfig
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {

    single {
        GsonBuilder().create()
    }

    single {
        OkHttpClient
            .Builder()
            .addInterceptor(AuthInterceptor(BuildConfig.NETWORK_KEY))
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
}