package hopeapps.dedev.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PullRequestDto(
    val url: String,
    val id: Long,
    @SerialName("node_id")
    val nodeId: String,
    @SerialName("html_url")
    val htmlUrl: String,
    val number: Int,
    val state: String,
    val locked: Boolean,
    val title: String,
    val user: UserDto,
    val body: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("closed_at")
    val closedAt: String? = null,
    @SerialName("merged_at")
    val mergedAt: String? = null,
    @SerialName("merge_commit_sha")
    val mergeCommitSha: String? = null,
    val draft: Boolean
)
