# PostKeeper
PostKeeper es una aplicación móvil creada para una prueba técnica. En este README, se mencionarán las características de la aplicación desde un punto de vista técnico.

## Introducción
PostKeeper es una aplicación móvil desarrollada en Kotlin para Android que utiliza el patrón de diseño MVVM (Model-View-ViewModel). El patrón MVVM se utiliza para separar los componentes de una aplicación en tres partes principales:

- Model (Modelo): Representa la capa de datos y lógica de negocio de la aplicación. Aquí se definen las estructuras de datos y las operaciones que se realizan en esos datos. El Modelo no tiene conocimiento de la interfaz de usuario.
- View (Vista): Representa la interfaz de usuario de la aplicación. Es la capa que el usuario ve y con la que interactúa. La Vista es responsable de mostrar los datos y recibir la entrada del usuario.
- ViewModel (Modelo de Vista): Actúa como intermediario entre el Modelo y la Vista. El ViewModel contiene la lógica de presentación y se encarga de transformar los datos del Modelo en un formato adecuado para que la Vista los muestre. También maneja las interacciones del usuario y comunica las acciones del usuario al Modelo si es necesario.

## Caracteristicas
Las principales características de la estructura del proyecto incluyen:

- Implementación de Koin como Inyección de dependencias.
- Uso de Retrofit como manejador de peticiones REST.
- Utilización de Databinding y LiveData para enlazar la vista (XML) con el ViewModel.
- Uso de Firestore como Backend para almacenar las entradas en una base de datos.
- Integración de Navigation Jetpack.
- Soporte para el modo oscuro.
- Cambio de idioma con respecto al dispositivo.

El diagrama de la arquitectura muestra tanto las activities como los fragments enlazados a sus respectivos viewmodels los cuales usan los repositorios conectados a los servicios de datos tanto Firestore como NewsAPI.

<img src="https://github.com/graco911/PostKeeper/assets/4141290/5695029f-9207-429e-87b9-b7d495d5d7e4" alt="Descripción de la imagen" width="50%" height="50%" />

## Aplicacion movil

PostKeeper consta de una pantalla principal con un BottomNavigationView que permite navegar entre la vista del listado de entradas y la vista de Noticias. También incluye un FloatingActionButton que muestra la pantalla para ingresar una nueva entrada y una vista para realizar búsquedas.

<img src="https://github.com/graco911/PostKeeper/assets/4141290/dfddadda-24cf-4bd3-8e50-7ed7049c65d4" alt="Descripción de la imagen" width="20%" height="20%" />
<img src="https://github.com/graco911/PostKeeper/assets/4141290/4a90582e-42d5-48a5-8488-94840d9c262e" alt="Descripción de la imagen" width="20%" height="20%" />

La aplicación incluye una validación que muestra u oculta la opción de ingresar nuevas entradas teniendo en cuenta si el dispositivo tiene o no conexión a Internet.

<img src="https://github.com/graco911/PostKeeper/assets/4141290/79012ebc-0ab1-4e9a-9d55-6c81e2dfd3b5" alt="Descripción de la imagen" width="20%" height="20%" />

El SearchView permite realizar búsquedas utilizando Android ChipView como filtros.

<img src="https://github.com/graco911/PostKeeper/assets/4141290/09b2257c-7f5e-43d4-b247-56369b86c58e" alt="Descripción de la imagen" width="20%" height="20%" />

```kotlin
private fun filterEntries(searchText: String, selectedChips: Set<Int>) {
        val allEntries = (viewModel.getPostsResultLiveData.value as UIStates.Success).value ?: emptyList()
        val filteredEntries = mutableListOf<BlogPostData>()

        for (entry in allEntries) {

            val shouldSearchInAllProperties = selectedChips.isEmpty()

            val propertyToFilter: String = when {
                shouldSearchInAllProperties -> "${entry.title} ${entry.author} ${entry.body}"
                R.id.chipOption1 in selectedChips -> entry.title
                R.id.chipOption2 in selectedChips -> entry.author
                R.id.chipOption3 in selectedChips -> entry.body
                else -> ""
            }

            // Aplica el filtrado según la propiedad seleccionada
            val matchesSearchText = propertyToFilter.contains(searchText, ignoreCase = true)
            if (matchesSearchText) {
                filteredEntries.add(entry)
            }
        }

        entriesListAdapter.submitAll(filteredEntries)
    }
```
## Pruebas
PostKeeper cuenta con pruebas unitarias e instrumentadas para agregar un registro a Firestore y probar el consumo de la API NewsAPI.

- Prueba instrumentada
```kotlin
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
```
- Test unitario
```kotlin
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
```
## NewsAPI
Aunque no se requiera el consumo del servicio de NewsAPI en la aplicación, se incluye para demostrar cómo se implementa Retrofit para consumir servicios REST.

## Mejoras a futuro
Algunas mejoras que se pueden considerar para el futuro incluyen:

- Implementar Compose.
- Conectar PostKeeper a un sistema de autenticación para crear cuentas de usuarios y mostrar registros por usuarios.
- Implementar notificaciones push.


