package com.gracodev.postkeeper.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gracodev.postkeeper.data.models.NewsResponseData
import com.gracodev.postkeeper.data.repositories.NewsRepository
import com.gracodev.postkeeper.data.usecases.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _newsLiveData = MutableLiveData<UseCaseResult<NewsResponseData>>()
    val newsLiveData: LiveData<UseCaseResult<NewsResponseData>> = _newsLiveData

    fun getNews(country: String, category: String, apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = newsRepository.getNews(country, category, apiKey)
            _newsLiveData.postValue(result)
        }
    }
}