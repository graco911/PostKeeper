package com.gracodev.postkeeper.data.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gracodev.postkeeper.data.repositories.BlogFirestoreRepository
import com.gracodev.postkeeper.ui.viewmodels.BlogViewModel

class BlogViewModelFactory(private val blogFirestoreRepository: BlogFirestoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlogViewModel::class.java)) {
            return BlogViewModel(blogFirestoreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}