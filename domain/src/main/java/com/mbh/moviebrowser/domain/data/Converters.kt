package com.mbh.moviebrowser.domain.data

import androidx.room.TypeConverter
import com.mbh.moviebrowser.api.di.SerializationModule
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

object Converters {

    val json = SerializationModule.provideKotlinSerialization()

    @TypeConverter
    fun stringToStringList(string: String): List<String> {
        return json.decodeFromString(ListSerializer(String.serializer()), string)
    }

    @TypeConverter
    fun stringsListToString(strings: List<String>): String {
        return json.encodeToString(ListSerializer(String.serializer()), strings)
    }

}