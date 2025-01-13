package com.klavs.football.room

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    private val gson= Gson()

    @TypeConverter
    fun fromString(value: String): List<String> {
        Log.d("Converters", "fromString called with: $value")
        return gson.fromJson(value, Array<String>::class.java).toList()
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        Log.d("Converters", "fromList called with: $list")
        return gson.toJson(list)
    }

}