package com.gracodev.postkeeper.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gracodev.postkeeper.data.models.BlogPostData
import com.gracodev.postkeeper.data.repositories.BlogFirestoreRepository
import com.gracodev.postkeeper.ui.states.UIStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlogViewModel(private val repository: BlogFirestoreRepository) : BaseViewModel() {

    // LiveData para comunicar el estado de las operaciones a la interfaz de usuario
    private val _addPostResultLiveData = MutableLiveData<UIStates<Unit>>()
    val addPostResultLiveData: LiveData<UIStates<Unit>> = _addPostResultLiveData

    private val _getPostsResultLiveData = MutableLiveData<UIStates<List<BlogPostData>>>()
    val getPostsResultLiveData: LiveData<UIStates<List<BlogPostData>>> = _getPostsResultLiveData

    private val _updatePostResultLiveData = MutableLiveData<UIStates<Unit>>()
    val updatePostResultLiveData: LiveData<UIStates<Unit>> = _updatePostResultLiveData

    private val _deletePostResultLiveData = MutableLiveData<UIStates<Unit>>()
    val deletePostResultLiveData: LiveData<UIStates<Unit>> = _deletePostResultLiveData

    private val _enabledButton =
        MutableLiveData<Boolean>().apply { value = false }
    val enabledButton: LiveData<Boolean> get() = _enabledButton

    private val _blogPostData = MutableLiveData<BlogPostData>().apply {
        value = BlogPostData("", "", 0, "")
    }
    val blogPostData: LiveData<BlogPostData> = _blogPostData

    private val _textChanged = MutableLiveData<Unit>()
    val textChanged: LiveData<Unit> = _textChanged

    // Función para agregar una nueva entrada de blog
    fun addBlogPost() {
        _blogPostData.value?.timeStamp = System.currentTimeMillis()
        _addPostResultLiveData.value = UIStates.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = blogPostData.value?.let { repository.addPost(it) }
            _addPostResultLiveData.postValue(result?.toUIStates())
        }
    }

    // Función para obtener todas las entradas de blog
    fun getBlogPosts() {
        _getPostsResultLiveData.value = UIStates.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getPosts()
            _getPostsResultLiveData.postValue(result.toUIStates())
        }
    }

    // Función para actualizar una entrada de blog existente
    fun updateBlogPost() {
        _updatePostResultLiveData.value = UIStates.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = blogPostData.value?.let { repository.updatePost(it) }
            _updatePostResultLiveData.postValue(result?.toUIStates())
        }
    }

    // Función para eliminar una entrada de blog por su ID
    fun deleteBlogPost() {
        _deletePostResultLiveData.value = UIStates.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = blogPostData.value?.id?.let { repository.deletePost(it) }
            _deletePostResultLiveData.postValue(result?.toUIStates())
        }
    }

    fun enableSaveButton(isVisible: Boolean) {
        _enabledButton.value = isVisible
    }

    fun setBlogPostData(blogPostData: BlogPostData) {
        this._blogPostData.value = blogPostData
    }

    fun notifyTextChanged() {
        _textChanged.value = Unit
    }
}