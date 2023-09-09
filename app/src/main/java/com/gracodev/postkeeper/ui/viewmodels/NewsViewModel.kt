package com.gracodev.postkeeper.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gracodev.postkeeper.data.models.NewsResponseData
import com.gracodev.postkeeper.data.repositories.NewsRepository
import com.gracodev.postkeeper.ui.states.UIStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(private val newsRepository: NewsRepository) : BaseViewModel() {

    private val _newsResultLiveData = MutableLiveData<UIStates<NewsResponseData>>()
    val newsResultLiveData: LiveData<UIStates<NewsResponseData>> = _newsResultLiveData

    fun getNews(country: String, category: String, apiKey: String) {
        _newsResultLiveData.value = UIStates.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = newsRepository.getNews(country, category, apiKey)
            _newsResultLiveData.postValue(result.toUIStates())
        }
    }
}