package hopeapps.dedev.feature_repo.presentation.details

sealed interface DetailEvent {
    data class OpenRepoInWeb(val url: String) : DetailEvent
    data class ShareRepo(val url: String) : DetailEvent
    object BackListener : DetailEvent
}