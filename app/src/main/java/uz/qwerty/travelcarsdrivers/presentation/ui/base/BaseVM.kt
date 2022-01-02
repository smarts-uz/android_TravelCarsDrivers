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

    protected val _loadingLiveData = MutableLiveData<Unit>()
    val loadingLiveData: LiveData<Unit> = _loadingLiveData

    protected val _emptyLiveData = MutableLiveData<Unit>()
    val emptyLiveData: LiveData<Unit> = _emptyLiveData

    protected val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    protected val _connectLiveData = MutableLiveData<Boolean>()
    val connectLiveData: LiveData<Boolean> = _connectLiveData

//    protected val _success = MutableLiveData<T>()
//    val success: LiveData<T> = _success

    protected fun launchViewModel(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job = viewModelScope.launch(context, start, block)

}