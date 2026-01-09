package hopeapps.dedev.feature_repo.data.datasource

import hopeapps.dedev.common.GitException
import hopeapps.dedev.common.Result
import hopeapps.dedev.core.database.dao.RepoDao
import hopeapps.dedev.core.database.dao.RepoLanguageDao
import hopeapps.dedev.core.database.dao.RepoReadmeDao
import hopeapps.dedev.core.database.model.RepoLanguageEntity
import hopeapps.dedev.core.database.model.RepoReadmeEntity
import hopeapps.dedev.core.database.model.RepositoryEntity
import hopeapps.dedev.feature_repo.domain.repository.RepoRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class RepoLocalDataSourceTest {

    private lateinit var repository: RepoRepository
    private lateinit var datasource: RepoLocalDataSource
    private val repoDao: RepoDao = mockk()
    private val repoLanguageDao: RepoLanguageDao = mockk()
    private val repoContentDao: RepoReadmeDao = mockk()

    @Before
    fun setup() {
        repository = mockk()
        datasource = RepoLocalDataSourceImpl(
            repoDao = repoDao,
            repoLanguageDao = repoLanguageDao,
            repoContentDao = repoContentDao
        )
    }

    @Test
    fun `fetchRepositoryById returns Success when dao returns repository`() = runTest {
        val repoId = 1L

        val repository = RepositoryEntity(
            id = repoId,
            name = "Repo",
            description = "",
            stars = 1,
            forks = 1,
            language = "Java",
            lastUpdate = "LastUpdate",
            isFork = false,
            repoOwner = "",
            watchers = 1,
            issues = 1
        )

        coEvery {
            repoDao.getRepositoryById(repoId)
        } returns repository

        val result = datasource.fetchRepositoryById(repoId)

        assert(result is Result.Success)
        assert((result as Result.Success).data == repository)
        coVerify(exactly = 1) {
            repoDao.getRepositoryById(repoId)
        }
        confirmVerified(repoDao)
    }

    @Test
    fun `fetchRepositoryById returns Error when dao throws exception`() = runTest {
        val repoId = 1L

        coEvery {
            repoDao.getRepositoryById(repoId)
        } throws Exception()

        val result = datasource.fetchRepositoryById(repoId)

        assert(result is Result.Error)
        assert((result as Result.Error).error is GitException.UnknownError)
        coVerify(exactly = 1) {
            repoDao.getRepositoryById(repoId)
        }
        confirmVerified(repoDao)
    }

    @Test
    fun `saveRepoLanguages calls insert on dao`() = runTest {
        val entity = RepoLanguageEntity(
            repoId = 1L,
            languages = listOf("Java", "Kotlin")
        )

        coEvery {
            repoLanguageDao.insert(entity)
        } just Runs

        datasource.saveRepoLanguages(entity)

        coVerify(exactly = 1) {
            repoLanguageDao.insert(entity)
        }

        confirmVerified(repoLanguageDao)
    }

    @Test
    fun `saveRepoContent calls insert on dao`() = runTest {
        val entity = RepoReadmeEntity(
            content = "",
            repoId = 0,
            id = 0
        )

        coEvery {
            repoContentDao.insert(entity)
        } just Runs

        datasource.saveRepoContent(entity)

        coVerify(exactly = 1) {
            repoContentDao.insert(entity)
        }

        confirmVerified(repoContentDao)
    }

    @Test
    fun `fetchRepoContent returns Success when dao returns content`() = runTest {
        val repoId = 1L

        val entity = RepoReadmeEntity(
            content = "",
            repoId = 0,
            id = 0
        )

        coEvery {
            repoContentDao.getByRepoId(repoId)
        } returns entity

        val result = datasource.fetchRepoContent(repoId)

        assert(result is Result.Success)
        assert((result as Result.Success).data == entity)
        coVerify(exactly = 1) {
            repoContentDao.getByRepoId(repoId)
        }
        confirmVerified(repoContentDao)
    }

    @Test
    fun `fetchRepoContent returns Error when dao throws exception`() = runTest {
        val repoId = 1L

        coEvery {
            repoContentDao.getByRepoId(repoId)
        } throws Exception()

        val result = datasource.fetchRepoContent(repoId)

        assert(result is Result.Error)
        assert((result as Result.Error).error is GitException.UnknownError)
        coVerify(exactly = 1) {
            repoContentDao.getByRepoId(repoId)
        }
        confirmVerified(repoContentDao)
    }

    @Test
    fun `fetchRepoLanguages returns Success when dao returns languages`() = runTest {
        val repoId = 1L

        val entity = RepoLanguageEntity(
            id = 0,
            repoId = 1,
            languages = listOf()
        )

        coEvery {
            repoLanguageDao.getByRepoId(repoId)
        } returns entity

        val result = datasource.fetchRepoLanguages(repoId)

        assert(result is Result.Success)
        assert((result as Result.Success).data == entity)
        coVerify(exactly = 1) {
            repoLanguageDao.getByRepoId(repoId)
        }
        confirmVerified(repoLanguageDao)
    }

    @Test
    fun `fetchRepoLanguages returns Error when dao throws exception`() = runTest {
        val repoId = 1L

        coEvery {
            repoLanguageDao.getByRepoId(repoId)
        } throws Exception()

        val result = datasource.fetchRepoLanguages(repoId)

        assert(result is Result.Error)
        assert((result as Result.Error).error == GitException.UnknownError)
        coVerify(exactly = 1) {
            repoLanguageDao.getByRepoId(repoId)
        }
        confirmVerified(repoLanguageDao)
    }

}
