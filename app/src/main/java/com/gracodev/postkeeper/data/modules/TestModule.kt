package com.gracodev.postkeeper.data.modules

import com.gracodev.postkeeper.R
import com.gracodev.postkeeper.data.factories.NewsViewModelFactory
import com.gracodev.postkeeper.data.interfaces.NewsAPI
import com.gracodev.postkeeper.data.repositories.NewsRepository
import com.gracodev.postkeeper.data.repositories.NewsRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

fun createTestAppModules(): Module = module {

    single {
        createWebService<NewsAPI>(
            okHttpClient = createHttpClient(get()),
            factory = RxJava2CallAdapterFactory.create(),
            baseUrl = androidContext().getString(R.string.news_api)
        )
    }

    single<NewsRepository> { NewsRepositoryImpl(get()) }
    factory { NewsViewModelFactory(get()) }
}