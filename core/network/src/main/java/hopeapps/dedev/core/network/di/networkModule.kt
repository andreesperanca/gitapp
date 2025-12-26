package hopeapps.dedev.core.network.di

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {

    single {
        GsonBuilder().create()
    }

    single {
        OkHttpClient.Builder().build()
    }

    single(named("USER")) {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
}