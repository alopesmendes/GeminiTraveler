package com.ippon.geminitraveler.data.datasource.database.converter

import androidx.room.TypeConverter
import java.time.Instant

class DateConverter {
    @TypeConverter
    fun fromEpochMilliToInstant(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun fromInstantToEpochMilli(value: Instant?): Long? {
        return value?.toEpochMilli()
    }
}