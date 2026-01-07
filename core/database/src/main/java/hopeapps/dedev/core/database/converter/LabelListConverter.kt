package hopeapps.dedev.core.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hopeapps.dedev.core.database.model.LabelEntity

class LabelListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromLabelList(labels: List<LabelEntity>): String {
        return gson.toJson(labels)
    }

    @TypeConverter
    fun toLabelList(json: String): List<LabelEntity> {
        val type = object : TypeToken<List<LabelEntity>>() {}.type
        return gson.fromJson(json, type)
    }
}