package com.gracodev.postkeeper.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.gracodev.postkeeper.data.models.BlogPostData
import com.gracodev.postkeeper.data.usecases.UseCaseResult
import kotlinx.coroutines.tasks.await

interface BlogFirestoreRepository {
    suspend fun addPost(post: BlogPostData): UseCaseResult<Unit>
    suspend fun getPosts(): UseCaseResult<List<BlogPostData>>
    suspend fun updatePost(post: BlogPostData): UseCaseResult<Unit>
    suspend fun deletePost(postId: String): UseCaseResult<Unit>
}

class BlogFirestoreRepositoryImpl(private val firestore: FirebaseFirestore) :
    BlogFirestoreRepository {

    private fun getBlogPostCollection() = firestore.collection("public_posts")

    override suspend fun addPost(post: BlogPostData): UseCaseResult<Unit> {
        return try {
            val collection = getBlogPostCollection()
            val document = collection.document()
            post.id = document.id
            document.set(post).await()
            UseCaseResult.Success(Unit)
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }

    override suspend fun getPosts(): UseCaseResult<List<BlogPostData>> {
        return try {
            val collection = getBlogPostCollection()
            val querySnapshot = collection.get().await()
            val postLists = mutableListOf<BlogPostData>()
            for (document in querySnapshot) {
                val news = document.toObject(BlogPostData::class.java)
                postLists.add(news)
            }
            UseCaseResult.Success(postLists)
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }

    override suspend fun updatePost(post: BlogPostData): UseCaseResult<Unit> {
        return try {
            val collection = getBlogPostCollection()
            collection.document(post.id).set(post).await()
            UseCaseResult.Success(Unit)
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }

    override suspend fun deletePost(postId: String): UseCaseResult<Unit> {
        return try {
            val collection = getBlogPostCollection()
            collection.document(postId).delete().await()
            UseCaseResult.Success(Unit)
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }
}