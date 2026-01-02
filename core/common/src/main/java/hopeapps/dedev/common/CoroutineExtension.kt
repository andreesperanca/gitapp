package hopeapps.dedev.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T> CoroutineScope.launchSuspend(
    block: suspend CoroutineScope.() -> T,
    onLoading: (Boolean) -> Unit = {},
    onResult: (T) -> Unit = {}
) {
    onLoading(true)
    launch {
        val result = withContext(Dispatchers.IO) { block() }
        onLoading(false)
        onResult(result)
    }
}