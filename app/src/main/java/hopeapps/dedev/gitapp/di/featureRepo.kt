package hopeapps.dedev.gitapp.di

import androidx.paging.ExperimentalPagingApi
import hopeapps.dedev.feature_repo.data.datasource.RepoLocalDataSource
import hopeapps.dedev.feature_repo.data.datasource.RepoLocalDataSourceImpl
import hopeapps.dedev.feature_repo.data.datasource.RepoRemoteDataSource
import hopeapps.dedev.feature_repo.data.datasource.RepoRemoteDataSourceImpl
import hopeapps.dedev.feature_repo.data.repository.RepoRepositoryImpl
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
import hopeapps.dedev.feature_repo.domain.usecase.IssueUseCase
import hopeapps.dedev.feature_repo.domain.usecase.PullRequestUseCase
import hopeapps.dedev.feature_repo.domain.usecase.RepoUseCase
import hopeapps.dedev.feature_repo.presentation.details.RepoDetailViewModel
import hopeapps.dedev.feature_repo.presentation.list.RepositoriesViewModel
import hopeapps.dedev.feature_repo.presentation.search.RepoSearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


@OptIn(ExperimentalPagingApi::class)
val featureRepo = module {
    single<RepoRepository> { RepoRepositoryImpl(get(), get(), get(), get()) }

    single<PullRequestUseCase> { PullRequestUseCase(get() )}
    single<IssueUseCase> { IssueUseCase(get() )}
    single<RepoUseCase> { RepoUseCase(get() )}

    single<RepoRemoteDataSource> { RepoRemoteDataSourceImpl(get()) }
    single<RepoLocalDataSource> { RepoLocalDataSourceImpl(get(), get(), get()) }


    viewModelOf(::RepositoriesViewModel)
    viewModelOf(::RepoSearchViewModel)
    viewModelOf(::RepoDetailViewModel)
}