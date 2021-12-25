package uz.qwerty.travelcarsdrivers.presentation.ui.common.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.qwerty.travelcarsdrivers.domain.repository.course.CourseRepository
import uz.qwerty.travelcarsdrivers.presentation.ui.base.BaseVM
import uz.qwerty.travelcarsdrivers.presentation.ui.state.Loading
import uz.qwerty.travelcarsdrivers.presentation.ui.state.Success
import uz.qwerty.travelcarsdrivers.presentation.ui.state.ViewState
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


    fun getAllCourse() {
        _courseLiveData.postValue(Loading)
        launchViewModel {
            val courseAll = repository.getCourse()
            _courseLiveData.postValue(Success(courseAll))
        }
    }

}