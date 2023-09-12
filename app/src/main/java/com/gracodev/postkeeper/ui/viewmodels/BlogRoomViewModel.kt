package com.gracodev.postkeeper.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gracodev.postkeeper.data.models.BlogPostData
import com.gracodev.postkeeper.data.repositories.BlogRoomRepository
import com.gracodev.postkeeper.ui.states.UIStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlogRoomViewModel(
    private val repository: BlogRoomRepository
) : BaseViewModel() {

    private val _addPostResultLiveData = MutableLiveData<UIStates<Unit>>()
    val addPostResultLiveData: LiveData<UIStates<Unit>> = _addPostResultLiveData

    private val _getPostsResultLiveData = MutableLiveData<UIStates<List<BlogPostData>>>()
    val getPostsResultLiveData: LiveData<UIStates<List<BlogPostData>>> = _getPostsResultLiveData

    private val _updatePostResultLiveData = MutableLiveData<UIStates<Unit>>()
    val updatePostResultLiveData: LiveData<UIStates<Unit>> = _updatePostResultLiveData

    private val _deletePostResultLiveData = MutableLiveData<UIStates<Unit>>()
    val deletePostResultLiveData: LiveData<UIStates<Unit>> = _deletePostResultLiveData

    fun updateBlogDB(posts: MutableList<BlogPostData>) {
        deleteAllPosts()
        for (post in posts) {
            addBlogPost(post)
        }
    }

    fun addBlogPost(post: BlogPostData) {
        post.timeStamp = System.currentTimeMillis()
        _addPostResultLiveData.value = UIStates.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.addPost(post)
            _addPostResultLiveData.postValue(result.toUIStates())
        }
    }

    fun getBlogPosts() {
        _getPostsResultLiveData.value = UIStates.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getPosts()
                _getPostsResultLiveData.postValue(result.toUIStates())
            } catch (ex: Exception) {
                _getPostsResultLiveData.postValue(UIStates.Error(ex.message ?: "Error desconocido"))
            }
        }
    }

    fun updateBlogPost(post: BlogPostData) {
        _updatePostResultLiveData.value = UIStates.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updatePost(post)
            _updatePostResultLiveData.postValue(result.toUIStates())
        }
    }

    fun deleteBlogPost(post: BlogPostData) {
        _deletePostResultLiveData.value = UIStates.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.deletePost(post)
            _deletePostResultLiveData.postValue(result.toUIStates())
        }
    }

    fun deleteAllPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.deleteAll()
            _deletePostResultLiveData.postValue(result.toUIStates())
        }
    }
}