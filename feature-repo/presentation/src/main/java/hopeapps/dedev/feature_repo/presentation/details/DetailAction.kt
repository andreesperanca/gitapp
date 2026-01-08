package hopeapps.dedev.feature_repo.presentation.details

sealed interface DetailAction {
    object OpenRepoInWeb : DetailAction
    object ShareRepo : DetailAction
    object BackListener : DetailAction
}