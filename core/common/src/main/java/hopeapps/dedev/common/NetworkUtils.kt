package hopeapps.dedev.common

import java.io.IOException


suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
):  Result <T> {
    return try {
        Result.Success(data = apiCall())
    } catch (e: IOException) {
        e.printStackTrace()
        Result.Error(GitException.NetworkError)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(GitException.UnknownError)
    }
}