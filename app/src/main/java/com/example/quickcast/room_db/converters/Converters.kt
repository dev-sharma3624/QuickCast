package com.example.quickcast.room_db.converters

import androidx.room.TypeConverter
import com.example.quickcast.data_classes.SmsFormats.SendableMessageProperty
import com.example.quickcast.enum_classes.SmsTypes

class Converters {
    @TypeConverter
    fun fromContactsList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toContactsList(data: String): List<String> {
        return if (data.isBlank() || data.isEmpty()) emptyList() else data.removePrefix(",").split(",")
    }

    @TypeConverter
    fun fromSmsType(type: SmsTypes): String = type.name

    @TypeConverter
    fun toSmsType(name: String): SmsTypes = SmsTypes.valueOf(name)
}
