package com.ippon.geminitraveler.data.datasource.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ippon.geminitraveler.core.utils.Constants
import java.time.Instant

@Entity(
    tableName = Constants.CHATS_TABLE
)
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    @ColumnInfo(name = "create_at")
    val createAt: Instant = Instant.now(),
)
