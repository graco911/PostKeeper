package com.gracodev.postkeeper.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blog_posts")
data class BlogPostData(
    @PrimaryKey(autoGenerate = true)
    var roomId: Int = 0,
    var title: String = "",
    var author: String = "",
    var timeStamp: Long = 0,
    var body: String = "",
    var id: String = ""
)