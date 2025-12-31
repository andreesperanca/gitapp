package hopeapps.dedev.core.network

import hopeapps.dedev.core.network.models.RepositoryDto
import hopeapps.dedev.core.network.models.SearchResponseDto
import hopeapps.dedev.core.network.models.UserDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitApi {

    @GET("users/{username}")
    suspend fun fetchRemoteUser(
        @Path("username") userFilterText: String
    ): UserDto

    @GET("users/{username}/repos")
    suspend fun fetchRemoteRepositories(
        @Path("username") userFilterText: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<RepositoryDto>

    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String? = null,
        @Query("order") order: String = "desc",
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchResponseDto



    @GET("repos/{owner}/{repo}/languages")
    suspend fun getLanguages(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Map<String, Int>

}