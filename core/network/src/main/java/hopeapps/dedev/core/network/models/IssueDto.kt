package hopeapps.dedev.core.network.models

import com.google.gson.annotations.SerializedName

data class IssueDto(
    val id: Long,
    val number: Int,
    val title: String,
    val state: String,
    val comments: Int,
    val url: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    val user: UserDto,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("closed_at")
    val closedAt: String?,
    val body: String?,
    @SerializedName("pull_request")
    val pullRequest: PullRequestInfoDto?,
    val labels: List<LabelDto>
)
