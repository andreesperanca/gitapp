package hopeapps.dedev.gitapp.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import hopeapps.dedev.core.database.model.RepositoryEntity
import hopeapps.dedev.feature_repo.data.datasource.RepoRemoteDataSource
import hopeapps.dedev.feature_repo.data.datasource.RepoRemoteDataSourceImpl
import hopeapps.dedev.feature_repo.data.datasource.RepoRemoteMediator
import hopeapps.dedev.feature_repo.data.repository.RepoRepositoryImpl
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
import hopeapps.dedev.feature_repo.domain.usecase.FetchRepositoryPaginatedUseCase
import hopeapps.dedev.feature_repo.domain.usecase.SearchRepositoryPaginatedUseCase
import hopeapps.dedev.feature_repo.presentation.list.RepositoriesViewModel
import hopeapps.dedev.feature_repo.presentation.search.RepoSearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


@OptIn(ExperimentalPagingApi::class)
val featureRepo = module {
    single<RepoRepository> { RepoRepositoryImpl(get(), get()) }
    single<RepoRemoteDataSource> { RepoRemoteDataSourceImpl(get()) }
    single<FetchRepositoryPaginatedUseCase> { FetchRepositoryPaginatedUseCase(get() ) }
    single<SearchRepositoryPaginatedUseCase> { SearchRepositoryPaginatedUseCase(get() )}
    viewModelOf(::RepositoriesViewModel)
    viewModelOf(::RepoSearchViewModel)
}