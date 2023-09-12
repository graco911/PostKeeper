package com.gracodev.postkeeper.data.repositories

import com.gracodev.postkeeper.data.dao.BlogPostDataDao
import com.gracodev.postkeeper.data.models.BlogPostData
import com.gracodev.postkeeper.data.usecases.UseCaseResult

interface BlogRoomRepository {
    suspend fun addPost(post: BlogPostData): UseCaseResult<Unit>
    suspend fun getPosts(): UseCaseResult<List<BlogPostData>>
    suspend fun getPostById(id: Long): UseCaseResult<BlogPostData>
    suspend fun updatePost(post: BlogPostData): UseCaseResult<Unit>
    suspend fun deletePost(post: BlogPostData): UseCaseResult<Unit>
    suspend fun deleteAll(): UseCaseResult<Unit>
}

class BlogRoomRepositoryImpl(private val blogPostDataDao: BlogPostDataDao) :
    BlogRoomRepository {
    override suspend fun addPost(post: BlogPostData): UseCaseResult<Unit> {
        return try {
            UseCaseResult.Success(blogPostDataDao.insert(post))
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }

    override suspend fun getPosts(): UseCaseResult<List<BlogPostData>> {
        return try {
            UseCaseResult.Success(blogPostDataDao.getAllBlogPosts())
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }

    override suspend fun getPostById(id: Long): UseCaseResult<BlogPostData> {
        return try {
            UseCaseResult.Success(blogPostDataDao.getBlogPostById(id))
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }

    override suspend fun updatePost(post: BlogPostData): UseCaseResult<Unit> {
        return try {
            UseCaseResult.Success(blogPostDataDao.update(post))
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }

    override suspend fun deletePost(post: BlogPostData): UseCaseResult<Unit> {
        return try {
            UseCaseResult.Success(blogPostDataDao.delete(post))
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }

    override suspend fun deleteAll(): UseCaseResult<Unit> {
        return try {
            UseCaseResult.Success(blogPostDataDao.deleteAll())
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }
}