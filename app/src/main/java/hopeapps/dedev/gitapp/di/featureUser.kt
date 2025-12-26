package hopeapps.dedev.gitapp.di

import hopeapps.dedev.feature_users.data.datasource.LocalDataSource
import hopeapps.dedev.feature_users.data.datasource.LocalDataSourceImpl
import hopeapps.dedev.feature_users.data.datasource.RemoteDataSource
import hopeapps.dedev.feature_users.data.datasource.RemoteDataSourceImpl
import hopeapps.dedev.feature_users.data.repository.UserRepositoryImpl
import hopeapps.dedev.feature_users.domain.repository.UserRepository
import hopeapps.dedev.feature_users.domain.usecase.FetchRecentUsersUseCase
import hopeapps.dedev.feature_users.domain.usecase.SearchUserUseCase
import hopeapps.dedev.feature_users.presentation.UserViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val featureUsers = module {
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<LocalDataSource> { LocalDataSourceImpl(get()) }
    single<RemoteDataSource> { RemoteDataSourceImpl(get()) }

    single<SearchUserUseCase> { SearchUserUseCase(get() ) }
    single<FetchRecentUsersUseCase> { FetchRecentUsersUseCase(get() ) }

    viewModelOf(::UserViewModel)
}