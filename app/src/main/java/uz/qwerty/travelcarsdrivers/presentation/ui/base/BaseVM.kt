package uz.qwerty.travelcarsdrivers.presentation.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * Created by Abdurashidov Shahzod on 24/12/21 22:07.
 * company
 * shahzod9933@gmail.com
 */
abstract class BaseVM : ViewModel() {

    protected val _loading = MutableLiveData<Unit>()
    val loading: LiveData<Unit> = _loading

    protected val _empty = MutableLiveData<Unit>()
    val empty: LiveData<Unit> = _empty

    protected val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

//    protected val _success = MutableLiveData<T>()
//    val success: LiveData<T> = _success

    protected fun launchViewModel(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job = viewModelScope.launch(context, start, block)

}