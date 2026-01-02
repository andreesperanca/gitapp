package hopeapps.dedev.common

sealed class GitException {
    object NetworkError : GitException()
    object UnknownError : GitException()
}