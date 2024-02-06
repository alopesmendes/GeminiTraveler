package com.ippon.geminitraveler.data.datasource.database.converter

import androidx.room.TypeConverter
import com.ippon.geminitraveler.domain.model.Role

class RoleConverter {
    @TypeConverter
    fun fromRoleToString(value: Role?): String? {
        return value?.name
    }

    @TypeConverter
    fun fromStringToRole(value: String?): Role? {
        return value?.let { Role.valueOf(it) }
    }
}