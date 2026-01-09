package hopeapps.dedev.feature_repo.presentation.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


fun formatDate(
    isoDate: String,
    pattern: String = "dd MMM yyyy"
): String {
    return runCatching {
        val instant = Instant.parse(isoDate)

        val formatter = DateTimeFormatter
            .ofPattern(pattern, Locale.getDefault())
            .withZone(ZoneId.systemDefault())

        formatter.format(instant)
    }.getOrElse { "" }
}