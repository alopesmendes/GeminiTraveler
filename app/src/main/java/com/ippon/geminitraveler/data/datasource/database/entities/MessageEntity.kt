package com.ippon.geminitraveler.data.datasource.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.ippon.geminitraveler.core.utils.Constants
import com.ippon.geminitraveler.domain.model.Role
import java.time.Instant

@Entity(
    tableName = Constants.MESSAGES_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["chat_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = MessageEntity::class,
            parentColumns = ["id"],
            childColumns = ["message_parent_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val message: String,
    val role: Role,
    @ColumnInfo(name = "create_at")
    val createAt: Instant = Instant.now(),
    @ColumnInfo(name = "chat_id")
    val chatId: Long,
    @ColumnInfo(name = "message_parent_id")
    val messageParentId: Long? = null
)
