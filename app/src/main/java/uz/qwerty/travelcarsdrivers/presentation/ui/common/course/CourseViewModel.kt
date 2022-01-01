package uz.qwerty.travelcarsdrivers.presentation.ui.common.course

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CurrencyItem
import uz.qwerty.travelcarsdrivers.domain.repository.course.CourseRepository
import uz.qwerty.travelcarsdrivers.presentation.ui.base.BaseVM
import uz.qwerty.travelcarsdrivers.presentation.ui.state.*
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
    private var _courseLiveData = MutableLiveData<ViewState>()
    val courseLiveData: LiveData<ViewState> get() = _courseLiveData

    private val _currencyLiveData = MutableLiveData<List<CurrencyItem>>()
    val currencyLiveData:LiveData<List<CurrencyItem>>get() = _currencyLiveData


    private val _currencyErrorLiveData = MutableLiveData<String>()
    val currencyErrorLiveData:LiveData<String>get() = _currencyErrorLiveData


    private var _state = MutableLiveData<ViewState>()
    val state: LiveData<ViewState> get() = _state

    private var _courseState = MutableStateFlow<ViewState>(Empty)
    val courseState: StateFlow<ViewState> get() = courseState


     fun getCourse() {
        _courseLiveData.value = Loading
        launchViewModel {
            val courseAll = repository.getCourse()
            when (courseAll) {
                is Success<*> -> {
                    Timber.tag("zarnigor").d("Zarnigor malumot keldi")
                }
                is ServerError -> {
                    Timber.tag("zarnigor").d("Zarnigor server error keldi")
                }
                is Fail -> {
                    Timber.tag("zarnigor").d("Zarnigor fail keldi ${courseAll.exception.message}")
                }
            }
            _courseLiveData.value = Success(courseAll)
        }
    }

    fun getAllCourse() {
        if (isConnected()) {
            _courseState.value = Loading
            launchViewModel(Dispatchers.IO) {
                repository.getCurrency().collect { response ->
                    response.onSuccess {
                        _currencyLiveData.postValue(it)
                        Timber.tag("zarnigor").d("Zarnigor malumot keldi")
                    }
                    response.onFailure {
                        _currencyErrorLiveData.postValue(it.message)
                        Timber.tag("zarnigor").d("Zarnigor server error keldi")
                    }
                }
            }
        }
    }

    fun newCourse() {
        _state.value = Loading
        launchViewModel {
            val newCourse = repository.getNewCourse()
            when(newCourse){
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