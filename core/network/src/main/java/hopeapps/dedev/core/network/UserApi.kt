package hopeapps.dedev.core.network

import hopeapps.dedev.core.network.models.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {

    @GET("users/{username}")
    suspend fun fetchRemoteUser(
        @Path("username") userFilterText: String
    ): UserDto

}