package com.ippon.geminitraveler.data.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ippon.geminitraveler.data.datasource.database.converter.DateConverter
import com.ippon.geminitraveler.data.datasource.database.converter.RoleConverter
import com.ippon.geminitraveler.data.datasource.database.dao.MessageDao
import com.ippon.geminitraveler.data.datasource.database.entities.MessageEntity
import org.koin.core.annotation.Single

@Database(
    entities = [MessageEntity::class],
    version = 1,
)
@TypeConverters(
    value = [RoleConverter::class, DateConverter::class]
)
abstract class GeminiLocalDatabase: RoomDatabase() {
    @Single
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile private var INSTANCE: GeminiLocalDatabase? = null

        @Single
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