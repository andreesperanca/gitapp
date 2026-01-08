package hopeapps.dedev.core.database.converter

import androidx.room.TypeConverter

class LanguageListConverter {

    @TypeConverter
    fun fromList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        if (value.isBlank()) return emptyList()
        return value.split(",")
    }
}