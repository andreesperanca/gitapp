package hopeapps.dedev.feature_repo.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import hopeapps.dedev.feature_repo.domain.entity.Issue
import hopeapps.dedev.feature_repo.domain.entity.Label
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
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

data class IssueUiModel(
    val id: Long,
    val title: String,
    val author: String,
    val createdAtFormatted: String,
    val labels: List<Label>
)

@RequiresApi(Build.VERSION_CODES.O)
fun Issue.toUiModel(): IssueUiModel {
    return IssueUiModel(
        id = id,
        title = title,
        author = author,
        createdAtFormatted = formatDate(createdAt),
        labels = labels
    )
}