package uz.qwerty.travelcarsdrivers.presentation.ui.common.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CurrencyItem
import uz.qwerty.travelcarsdrivers.domain.models.NewCurrencyResponse
import uz.qwerty.travelcarsdrivers.domain.repository.course.CourseRepository
import uz.qwerty.travelcarsdrivers.presentation.ui.base.BaseVM
import uz.qwerty.travelcarsdrivers.presentation.ui.state.*
import uz.qwerty.travelcarsdrivers.util.Event
import uz.qwerty.travelcarsdrivers.util.isConnected
import javax.inject.Inject


/**
 * Created by Abdurashidov Shahzod on 24/12/21 22:13.
 * company
 * shahzod9933@gmail.com
 */
@HiltViewModel
class CourseViewModel @Inject constructor(
    private val repository: CourseRepository
) : BaseVM() {
    private var _courseLiveData = MutableLiveData<Event<ViewState>>()
    val courseLiveData: LiveData<Event<ViewState>> get() = _courseLiveData


    private var _currencyNewLiveData = MutableLiveData<Event<ViewState>>()
    val currencyNewLiveData: LiveData<Event<ViewState>> get() = _currencyNewLiveData


    private val _currencyLiveData = MutableLiveData<List<CurrencyItem>>()
    val currencyLiveData: LiveData<List<CurrencyItem>> get() = _currencyLiveData

    private val _currencyErrorLiveData = MutableLiveData<String>()
    val currencyErrorLiveData: LiveData<String> get() = _currencyErrorLiveData

    private var _state = MutableLiveData<ViewState>()
    val state: LiveData<ViewState> get() = _state

    private var _courseState = MutableStateFlow<ViewState>(Empty)
    val courseState: StateFlow<ViewState> get() = courseState

    init {
        newCurrencyCourse()
    }

    fun getCourse() {
        _courseLiveData.value = Event(Loading)
        viewModelScope.launch {
            val event = repository.getCourse()
            when (event) {
                is Success<*> -> {
                    Timber.tag("zarnigor").d("Zarnigor malumot keldi")
                }
                is ServerError -> {
                    Timber.tag("zarnigor").d("Zarnigor server error keldi")
                }
                is Fail -> {
                    Timber.tag("zarnigor").d("Zarnigor fail keldi ${event.exception.message}")
                }
                else -> {
                    Unit
                }
            }
            _courseLiveData.value = Event(event)
        }
    }

    private fun newCurrencyCourse() {
        _currencyNewLiveData.value = Event(Loading)
        viewModelScope.launch {
            val event = repository.newCurrency()
            when (event) {
                is Success<*> -> {
                    Timber.tag("zarnigor").d("Zarnigor malumot keldi")
                }
                is ServerError -> {
                    Timber.tag("zarnigor").d("Zarnigor server error keldi")
                }
                is Fail -> {
                    Timber.tag("zarnigor").d("Zarnigor fail keldi ${event.exception.message}")
                }
                else -> {
                    Unit
                }
            }
            _currencyNewLiveData.value = Event(event)
        }
    }

    fun getAllCourse() {
        if (isConnected()) {
            _loadingLiveData.value = Unit
            launchViewModel(Dispatchers.IO) {
                repository.getCurrency().collect { response ->
                    response.onSuccess {
                        _currencyLiveData.postValue(it)
                        Timber.tag("zarnigor").d("Zarnigor malumot keldi")
                    }
                    response.onFailure {
                        _errorLiveData.postValue(it.message)
                        Timber.tag("zarnigor").d("Zarnigor server error keldi")
                    }
                }
            }
        } else {
            _connectLiveData.value = true
        }
    }

    fun newCourse() {
        _state.value = Loading
        launchViewModel {
            val newCourse = repository.getNewCourse()
            when (newCourse) {
                is Success<*> -> {
                    Timber.tag("zarnigor").d("Zarnigor malumot keldi")
                }
                is ServerError -> {
                    Timber.tag("zarnigor").d("Zarnigor server error keldi")
                }
                is Fail -> {
                    Timber.tag("zarnigor").d("Zarnigor fail keldi ${newCourse.exception.message}")
                }
            }
            _state.value = Success(newCourse)
        }
    }

}