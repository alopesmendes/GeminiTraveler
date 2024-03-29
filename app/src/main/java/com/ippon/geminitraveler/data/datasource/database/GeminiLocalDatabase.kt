package com.ippon.geminitraveler.data.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ippon.geminitraveler.data.datasource.database.converter.DateConverter
import com.ippon.geminitraveler.data.datasource.database.converter.RoleConverter
import com.ippon.geminitraveler.data.datasource.database.dao.ChatDao
import com.ippon.geminitraveler.data.datasource.database.dao.MessageDao
import com.ippon.geminitraveler.data.datasource.database.entities.ChatEntity
import com.ippon.geminitraveler.data.datasource.database.entities.MessageEntity

@Database(
    entities = [MessageEntity::class, ChatEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(
    value = [RoleConverter::class, DateConverter::class]
)
abstract class GeminiLocalDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao

    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile private var INSTANCE: GeminiLocalDatabase? = null

        fun getInstance(context: Context): GeminiLocalDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                GeminiLocalDatabase::class.java, "gemini_local_database.db")
                // prepopulate the database after onCreate was called
                .build()
    }
}