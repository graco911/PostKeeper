package com.gracodev.postkeeper.data.modules

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import com.gracodev.postkeeper.R
import com.gracodev.postkeeper.data.database.AppDatabase
import com.gracodev.postkeeper.data.factories.BlogRoomViewModelFactory
import com.gracodev.postkeeper.data.factories.BlogViewModelFactory
import com.gracodev.postkeeper.data.factories.NewsViewModelFactory
import com.gracodev.postkeeper.data.interfaces.NewsAPI
import com.gracodev.postkeeper.data.repositories.BlogFirestoreRepository
import com.gracodev.postkeeper.data.repositories.BlogFirestoreRepositoryImpl
import com.gracodev.postkeeper.data.repositories.BlogRoomRepository
import com.gracodev.postkeeper.data.repositories.BlogRoomRepositoryImpl
import com.gracodev.postkeeper.data.repositories.NewsRepository
import com.gracodev.postkeeper.data.repositories.NewsRepositoryImpl
import com.gracodev.postkeeper.interceptors.NetworkConnectionInterceptor
import com.gracodev.postkeeper.ui.viewmodels.BlogRoomViewModel
import com.gracodev.postkeeper.ui.viewmodels.BlogViewModel
import com.gracodev.postkeeper.ui.viewmodels.ConnectivityViewModel
import com.gracodev.postkeeper.ui.viewmodels.NewsViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun createAppModules(): Module = module {

    single {
        createWebService<NewsAPI>(
            okHttpClient = createHttpClient(get()),
            factory = RxJava2CallAdapterFactory.create(),
            baseUrl = androidContext().getString(R.string.news_api)
        )
    }

    single {
        Room.databaseBuilder(
            androidApplication(), AppDatabase::class.java,
            "postkeeper_db"
        )
            .build()
    }

    single { FirebaseFirestore.getInstance() }
    single {
        val database = get<AppDatabase>()
        database.blogPostDataDao()
    }
    single<BlogRoomRepository> { BlogRoomRepositoryImpl(get()) }
    single<NewsRepository> { NewsRepositoryImpl(get()) }
    single<BlogFirestoreRepository> { BlogFirestoreRepositoryImpl(get()) }

    factory { NewsViewModelFactory(get()) }
    factory { BlogViewModelFactory(get()) }
    factory { BlogRoomViewModelFactory(get()) }

    viewModel { BlogRoomViewModel(get()) }
    viewModel { NewsViewModel(get()) }
    viewModel { BlogViewModel(get()) }
    viewModel { ConnectivityViewModel(get()) }
}

fun createHttpClient(context: Context): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(NetworkConnectionInterceptor(context))
        .readTimeout(5, TimeUnit.MINUTES)
        .retryOnConnectionFailure(true)
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder().method(original.method, original.body).build()
            chain.proceed(request)
        }
        .build()
}

inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient,
    factory: CallAdapter.Factory,
    baseUrl: String
): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addCallAdapterFactory(factory)
        .client(okHttpClient)
        .build()
    return retrofit.create(T::class.java)
}