package com.gracodev.postkeeper

import androidx.test.ext.junit.runners.AndroidJUnit4
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
        firestore = FirebaseFirestore.getInstance()

        repository = BlogFirestoreRepositoryImpl(firestore)
    }

    @Test
    fun addPost_shouldReturnUseCaseResultSuccess() = runBlocking {
        // Arrange: Crea un objeto BlogPostData para agregar
        val blogPost = BlogPostData(
            title = "Hola Mundo",
            author = "Carlos Graniel",
            timeStamp = System.currentTimeMillis(),
            body = "Esta es una publicación que estoy creando desde una prueba instrumentada en Android. Esta prueba carga un registro directamente en Firestore y sirve como demostración del correcto funcionamiento de la aplicación conectada a Firebase.",
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