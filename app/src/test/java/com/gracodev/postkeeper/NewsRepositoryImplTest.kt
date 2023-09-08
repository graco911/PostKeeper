package com.gracodev.postkeeper

import com.gracodev.postkeeper.data.interfaces.NewsAPI
import com.gracodev.postkeeper.data.models.Article
import com.gracodev.postkeeper.data.models.NewsResponseData
import com.gracodev.postkeeper.data.models.Source
import com.gracodev.postkeeper.data.repositories.NewsRepository
import com.gracodev.postkeeper.data.repositories.NewsRepositoryImpl
import com.gracodev.postkeeper.data.usecases.UseCaseResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class NewsRepositoryImplTest {

    private lateinit var newsRepository: NewsRepository
    private lateinit var newsAPI: NewsAPI

    @Before
    fun setup() {
        newsAPI = mockk()
        newsRepository = NewsRepositoryImpl(newsAPI)
    }

    @Test
    fun `getNews success`() = runBlocking {
        // Configura el objeto de respuesta simulada
        val expectedResponse = NewsResponseData(
            status = "ok",
            totalResults = 1,
            articles = listOf(
                Article(
                    source = Source(
                        id = "google-news",
                        name = ""
                    ),
                    author = "",
                    title = "",
                    description = "",
                    url = "",
                    urlToImage = "",
                    publishedAt = "",
                    content = ""
                )
            )
        )
        val response = Response.success(expectedResponse)
        coEvery { newsAPI.getNews(any(), any(), any()) } returns createDeferredResponse(response)

        // Llama al método que se está probando
        val result = newsRepository.getNews("us", "business", "your_api_key")

        // Verifica que el resultado sea un éxito y que los datos sean los esperados
        assert(result is UseCaseResult.Success)
        val successResult = result as UseCaseResult.Success
        assert(successResult.data == expectedResponse)
    }

    // Función de utilidad para crear Deferred<Response<T>> simulado
    private fun <T : Any> createDeferredResponse(data: Response<T>): Deferred<Response<T>> {
        return GlobalScope.async { data }
    }
}