package hopeapps.dedev.core.network.models

import com.google.gson.annotations.SerializedName

data class LabelDto(
    val id: Long,
    val name: String,
    @SerializedName("color")
    val colorHex: Int
)