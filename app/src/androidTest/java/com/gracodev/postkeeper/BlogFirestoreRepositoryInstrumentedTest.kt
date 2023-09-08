package com.gracodev.postkeeper

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.firestore.FirebaseFirestore
import com.gracodev.postkeeper.data.models.BlogPostData
import com.gracodev.postkeeper.data.repositories.BlogFirestoreRepository
import com.gracodev.postkeeper.data.repositories.BlogFirestoreRepositoryImpl
import com.gracodev.postkeeper.data.usecases.UseCaseResult
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BlogFirestoreRepositoryImplInstrumentedTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var repository: BlogFirestoreRepository

    @Before
    fun setUp() {
        // Obtén el contexto de la aplicación instrumentada
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        firestore = FirebaseFirestore.getInstance()

        repository = BlogFirestoreRepositoryImpl(firestore)
    }

    @Test
    fun addPost_shouldReturnUseCaseResultSuccess() = runBlocking {
        // Arrange: Crea un objeto BlogPostData para agregar
        val blogPost = BlogPostData(
            title = "Título de la publicación",
            author = "Autor de la publicación",
            timeStamp = System.currentTimeMillis(),
            body = "Contenido de la publicación",
            id = ""
        )

        // Act: Llama a la función para agregar un post
        val result = repository.addPost(blogPost)

        // Assert: Verifica que el resultado sea un UseCaseResult.Success
        assert(result is UseCaseResult.Success)
    }

    @Test
    fun getPosts_shouldReturnUseCaseResultSuccess() = runBlocking {
        // Act: Llama a la función para obtener los posts
        val result = repository.getPosts()

        // Assert: Verifica que el resultado sea un UseCaseResult.Success
        assert(result is UseCaseResult.Success)
    }
}