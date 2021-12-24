package uz.qwerty.travelcarsdrivers.presentation.ui.common.course

import dagger.hilt.android.lifecycle.HiltViewModel
import uz.qwerty.travelcarsdrivers.domain.repository.course.CourseRepository
import uz.qwerty.travelcarsdrivers.presentation.ui.base.BaseVM
import javax.inject.Inject


/**
 * Created by Abdurashidov Shahzod on 24/12/21 22:13.
 * company
 * shahzod9933@gmail.com
 */
@HiltViewModel
class CourseViewModel @Inject constructor(
    private val repository: CourseRepository
) :BaseVM() {

}