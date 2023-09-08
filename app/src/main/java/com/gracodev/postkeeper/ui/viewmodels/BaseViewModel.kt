package com.gracodev.postkeeper.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.gracodev.postkeeper.data.usecases.UseCaseResult
import com.gracodev.postkeeper.ui.states.UIStates

open class BaseViewModel : ViewModel() {

    fun <T : Any> UseCaseResult<T>.toUIStates(): UIStates<T> {
        return when (this) {
            is UseCaseResult.Success -> UIStates.Success(this.data)
            is UseCaseResult.Error -> UIStates.Error(this.exception.message ?: "An error occurred")
        }
    }
}