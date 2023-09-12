package com.gracodev.postkeeper.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gracodev.postkeeper.data.models.BlogPostData

@Dao
interface BlogPostDataDao {
    @Insert
    suspend fun insert(blogPostData: BlogPostData)

    @Query("SELECT * FROM blog_posts")
    suspend fun getAllBlogPosts(): List<BlogPostData>

    @Query("SELECT * FROM blog_posts WHERE roomId = :id")
    suspend fun getBlogPostById(id: Long): BlogPostData

    @Update
    suspend fun update(blogPostData: BlogPostData)

    @Delete
    fun delete(blogPostData: BlogPostData)

    @Query("DELETE FROM blog_posts")
    fun deleteAll()
}