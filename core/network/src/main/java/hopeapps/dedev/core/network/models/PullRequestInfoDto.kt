package hopeapps.dedev.core.network.models

import com.google.gson.annotations.SerializedName

data class PullRequestInfoDto(
    val url: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("diff_url")
    val diffUrl: String,
    @SerializedName("patch_url")
    val patchUrl: String,
    @SerializedName("merged_at")
    val mergedAt: String?
)
