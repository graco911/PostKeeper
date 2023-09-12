package com.gracodev.postkeeper.data.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gracodev.postkeeper.data.repositories.BlogRoomRepository
import com.gracodev.postkeeper.ui.viewmodels.BlogRoomViewModel

class BlogRoomViewModelFactory(
    private val blogRoomRepository: BlogRoomRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlogRoomViewModel::class.java)) {
            return BlogRoomViewModel(blogRoomRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}