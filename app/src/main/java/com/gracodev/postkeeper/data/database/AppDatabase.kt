package com.gracodev.postkeeper.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gracodev.postkeeper.data.dao.BlogPostDataDao
import com.gracodev.postkeeper.data.models.BlogPostData

@Database(entities = [BlogPostData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun blogPostDataDao(): BlogPostDataDao

    companion object {
        private const val DATABASE_NAME = "myapp-database"

        fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}