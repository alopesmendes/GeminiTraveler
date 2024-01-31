package com.ippon.geminitraveler.data.datasource.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.ippon.geminitraveler.core.utils.Constants

@Entity(
    tableName = Constants.MESSAGES_TABLE,
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val message: String,
    @ColumnInfo(name = "is_gemini")
    val isGemini: Boolean,
)
