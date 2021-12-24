package uz.qwerty.travelcarsdrivers.domain.repository.course

import retrofit2.Response
import uz.qwerty.travelcarsdrivers.data.remote.api.CourseApi
import uz.qwerty.travelcarsdrivers.presentation.ui.state.Fail
import uz.qwerty.travelcarsdrivers.presentation.ui.state.ServerError
import uz.qwerty.travelcarsdrivers.presentation.ui.state.Success
import uz.qwerty.travelcarsdrivers.presentation.ui.state.ViewState
import javax.inject.Inject


/**
 * Created by Abdurashidov Shahzod on 24/12/21 21:46.
 * company QQBank
 * shahzod9933@gmail.com
 */

class CourseRepositoryImpl @Inject constructor(
    private val api: CourseApi
) : CourseRepository {
    override suspend fun getCourse(): ViewState {
        return try {
            val course = api.getCourse()
            if (course.isSuccessful) {
                Success(course.body())
            } else {
                ServerError(course.message(), course.code())
            }
        } catch (e: Exception) {
            Fail(e)
        }
    }
}