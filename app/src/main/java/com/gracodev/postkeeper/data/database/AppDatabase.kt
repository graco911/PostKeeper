package com.gracodev.postkeeper.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gracodev.postkeeper.data.dao.BlogPostDataDao
import com.gracodev.postkeeper.data.models.BlogPostData

@Database(entities = [BlogPostData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun blogPostDataDao(): BlogPostDataDao
}