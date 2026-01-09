package hopeapps.dedev.feature_repo.data.repository

import hopeapps.dedev.common.GitException
import hopeapps.dedev.core.database.AppDatabase
import hopeapps.dedev.core.database.model.RepositoryEntity
import hopeapps.dedev.core.network.GitApi
import hopeapps.dedev.feature_repo.data.datasource.RepoLocalDataSource
import hopeapps.dedev.feature_repo.data.datasource.RepoRemoteDataSource
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import hopeapps.dedev.common.Result.*
import hopeapps.dedev.core.database.model.RepoLanguageEntity
import hopeapps.dedev.core.database.model.RepoReadmeEntity
import hopeapps.dedev.core.network.models.RepoReadmeDto
import hopeapps.dedev.feature_repo.data.mapper.toDomain
import hopeapps.dedev.feature_repo.data.mapper.toEntity
import io.mockk.Runs
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just


class RepoRepositoryImplTest {

    private lateinit var repository: RepoRepository
    private val gitApi: GitApi = mockk()
    private val db: AppDatabase = mockk()
    private val repoRemoteDataSource: RepoRemoteDataSource = mockk()
    private val repoLocalDataSource: RepoLocalDataSource = mockk()

    @Before
    fun setup() {

        repository = RepoRepositoryImpl(
            gitApi = gitApi,
            db = db,
            repoRemoteDataSource = repoRemoteDataSource,
            repoLocalDataSource = repoLocalDataSource
        )

    }

    @Test
    fun `fetchRepoById returns Success with domain model when local returns Success`() = runTest {
        val repoId = 1L

        val entity = RepositoryEntity(
            id = repoId,
            name = "Repo",
            repoOwner = "Owner",
            stars = 10,
            forks = 5,
            watchers = 20,
            issues = 1,
            description = "desc",
            language = "Kotlin",
            lastUpdate = "2024-01-01",
            isFork = false
        )

        coEvery {
            repoLocalDataSource.fetchRepositoryById(repoId)
        } returns Success(entity)

        val result = repository.fetchRepoById(repoId)

        assert(result is Success)

        val domain = (result as Success).data
        assert(domain.id == entity.id)
        assert(domain.name == entity.name)
        assert(domain.repoOwner == entity.repoOwner)

        coVerify(exactly = 1) {
            repoLocalDataSource.fetchRepositoryById(repoId)
        }

        confirmVerified(repoLocalDataSource)
    }

    @Test
    fun `fetchRepoById returns Error when local returns Error`() = runTest {
        val repoId = 1L
        val error = GitException.UnknownError

        coEvery {
            repoLocalDataSource.fetchRepositoryById(repoId)
        } returns Error(error)

        val result = repository.fetchRepoById(repoId)

        assert(result is Error)
        assert((result as Error).error == error)

        coVerify(exactly = 1) {
            repoLocalDataSource.fetchRepositoryById(repoId)
        }

        confirmVerified(repoLocalDataSource)
    }

    @Test
    fun `fetchRepositoryReadme returns Success and saves locally when remote returns Success`() = runTest {
        val repoId = 1L
        val owner = "android"
        val repo = "compose"

        val dto = RepoReadmeDto(
            content = "README"
        )

        coEvery {
            repoRemoteDataSource.fetchRepositoryReadme(owner, repo)
        } returns Success(dto)

        coEvery {
            repoLocalDataSource.saveRepoContent(any())
        } just Runs

        val result = repository.fetchRepositoryReadme(owner, repo, repoId)

        assert(result is Success)
        assert((result as Success).data == dto.toDomain())

        coVerify(exactly = 1) {
            repoRemoteDataSource.fetchRepositoryReadme(owner, repo)
            repoLocalDataSource.saveRepoContent(dto.toEntity(repoId))
        }

        confirmVerified(repoRemoteDataSource, repoLocalDataSource)
    }

    @Test
    fun `fetchRepositoryReadme returns local data when remote fails and local has data`() = runTest {
        val repoId = 1L
        val owner = "android"
        val repo = "compose"

        val error = GitException.NetworkError

        val entity = RepoReadmeEntity(
            repoId = repoId,
            content = "LOCAL README"
        )

        coEvery {
            repoRemoteDataSource.fetchRepositoryReadme(owner, repo)
        } returns Error(error)

        coEvery {
            repoLocalDataSource.fetchRepoContent(repoId)
        } returns Success(entity)

        val result = repository.fetchRepositoryReadme(owner, repo, repoId)

        assert(result is Success)
        assert((result as Success).data == entity.toDomain())

        coVerify(exactly = 1) {
            repoRemoteDataSource.fetchRepositoryReadme(owner, repo)
            repoLocalDataSource.fetchRepoContent(repoId)
        }

        confirmVerified(repoRemoteDataSource, repoLocalDataSource)
    }

    @Test
    fun `fetchRepositoryReadme returns Error when both remote and local fail`() = runTest {
        val repoId = 1L
        val owner = "android"
        val repo = "compose"

        val error = GitException.UnknownError

        coEvery {
            repoRemoteDataSource.fetchRepositoryReadme(owner, repo)
        } returns Error(error)

        coEvery {
            repoLocalDataSource.fetchRepoContent(repoId)
        } returns Error(error = error)

        val result = repository.fetchRepositoryReadme(owner, repo, repoId)

        assert(result is Error)
        assert((result as Error).error == error)

        coVerify(exactly = 1) {
            repoRemoteDataSource.fetchRepositoryReadme(owner, repo)
            repoLocalDataSource.fetchRepoContent(repoId)
        }

        confirmVerified(repoRemoteDataSource, repoLocalDataSource)
    }

    @Test
    fun `fetchRepoLanguages returns Success and saves locally when remote returns Success`() = runTest {
        val repoId = 1L
        val owner = "android"
        val repo = "compose"

        val remoteResponse = mapOf(
            "Kotlin" to 80,
            "Java" to 20
        )

        coEvery {
            repoRemoteDataSource.fetchRepositoryLanguages(owner, repo)
        } returns Success(remoteResponse)

        coEvery {
            repoLocalDataSource.saveRepoLanguages(any())
        } just Runs

        val result = repository.fetchRepoLanguages(owner, repo, repoId)

        assert(result is Success)
        assert((result as Success).data == remoteResponse.toDomain())

        coVerify(exactly = 1) {
            repoRemoteDataSource.fetchRepositoryLanguages(owner, repo)
            repoLocalDataSource.saveRepoLanguages(
                remoteResponse.toEntity(repoId)
            )
        }

        confirmVerified(repoRemoteDataSource, repoLocalDataSource)
    }

    @Test
    fun `fetchRepoLanguages returns local data when remote fails and local has data`() = runTest {
        val repoId = 1L
        val owner = "android"
        val repo = "compose"

        val error = GitException.NetworkError

        val localEntity = RepoLanguageEntity(id = 0, repoId, languages = listOf())

        coEvery {
            repoRemoteDataSource.fetchRepositoryLanguages(owner, repo)
        } returns Error(error)

        coEvery {
            repoLocalDataSource.fetchRepoLanguages(repoId)
        } returns Success(localEntity)

        val result = repository.fetchRepoLanguages(owner, repo, repoId)

        assert(result is Success)
        assert((result as Success).data == localEntity.toDomain())

        coVerify(exactly = 1) {
            repoRemoteDataSource.fetchRepositoryLanguages(owner, repo)
            repoLocalDataSource.fetchRepoLanguages(repoId)
        }

        confirmVerified(repoRemoteDataSource, repoLocalDataSource)
    }

    @Test
    fun `fetchRepoLanguages returns Error when both remote and local fail`() = runTest {
        val repoId = 1L
        val owner = "android"
        val repo = "compose"

        val error = GitException.UnknownError

        coEvery {
            repoRemoteDataSource.fetchRepositoryLanguages(owner, repo)
        } returns Error(error)

        coEvery {
            repoLocalDataSource.fetchRepoLanguages(repoId)
        } returns Error(error)

        val result = repository.fetchRepoLanguages(owner, repo, repoId)

        assert(result is Error)
        assert((result as Error).error == error)

        coVerify(exactly = 1) {
            repoRemoteDataSource.fetchRepositoryLanguages(owner, repo)
            repoLocalDataSource.fetchRepoLanguages(repoId)
        }

        confirmVerified(repoRemoteDataSource, repoLocalDataSource)
    }

}