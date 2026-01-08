package hopeapps.dedev.gitapp.di

import hopeapps.dedev.feature_users.data.datasource.UserLocalDataSource
import hopeapps.dedev.feature_users.data.datasource.LocalDataSourceImpl
import hopeapps.dedev.feature_users.data.datasource.UserRemoteDataSource
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
    single<UserLocalDataSource> { LocalDataSourceImpl(get()) }
    single<UserRemoteDataSource> { RemoteDataSourceImpl(get()) }
    single<SearchUserUseCase> { SearchUserUseCase(get() ) }
    single<FetchRecentUsersUseCase> { FetchRecentUsersUseCase(get() ) }
    viewModelOf(::UserViewModel)
}