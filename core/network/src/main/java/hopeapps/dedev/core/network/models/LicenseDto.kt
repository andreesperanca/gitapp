package hopeapps.dedev.core.network.models

import com.google.gson.annotations.SerializedName

data class LicenseDto(
    @SerializedName("key")
    val key: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("spdx_id")
    val spdxId: String,

    @SerializedName("url")
    val url: String?,

    @SerializedName("node_id")
    val nodeId: String
)