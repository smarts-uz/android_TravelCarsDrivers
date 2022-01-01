package uz.qwerty.travelcarsdrivers.domain.repository.course

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import uz.qwerty.travelcarsdrivers.data.remote.api.CourseApi
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CourseResponse
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CourseResponseItem
import uz.qwerty.travelcarsdrivers.presentation.ui.state.ViewState


/**
 * Created by Abdurashidov Shahzod on 24/12/21 21:46.
 * company QQBank
 * shahzod9933@gmail.com
 */

interface CourseRepository {
    suspend fun getCourse(): ViewState
    fun getCourseAll(): Flow<Result<List<CourseResponseItem>>>
}