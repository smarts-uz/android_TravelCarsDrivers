package uz.qwerty.travelcarsdrivers.presentation.ui.common.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
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

    private var _courseState = MutableStateFlow<ViewState>(Empty)
    val courseState: StateFlow<ViewState> get() = courseState


    fun getCourse() {
        _courseLiveData.postValue(Loading)
        launchViewModel {
            val courseAll = repository.getCourse()
            _courseLiveData.postValue(Success(courseAll))
        }
    }

    fun getAllCourse() {
        if (isConnected()) {
            _courseState.value = Loading
            launchViewModel(Dispatchers.IO) {
                repository.getCourseAll().collect { response ->
                    response.onSuccess {
                        _courseState.value = Success(it)
                    }
                }
            }
        }
    }

}