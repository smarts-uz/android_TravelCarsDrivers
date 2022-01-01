package uz.qwerty.travelcarsdrivers.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.data.remote.api.CourseApi
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CourseResponse
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CourseResponseItem
import uz.qwerty.travelcarsdrivers.domain.repository.course.CourseRepository
import uz.qwerty.travelcarsdrivers.presentation.app.App
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
            if (course.success) {
                Success(course.data)
            } else {
                ServerError(course.error.message,course.error.code)
            }
        } catch (e: Exception) {
            Fail(e)
        }
    }

    override fun getCourseAll(): Flow<Result<List<CourseResponseItem>>> = flow {
//        try {
//            val responseCourse = api.getCourse()
//            if (responseCourse.code() == 200) responseCourse.body()?.let {
//                emit(Result.success<List<CourseResponseItem>>(it))
//            } else {
//                emit(Result.failure<List<CourseResponseItem>>(Exception(App.instance.getString(R.string.error))))
//            }
//        } catch (e: Exception) {
//            Timber.e("Repository in function refreshToken error = $e")
//            emit(Result.failure<List<CourseResponseItem>>(e))
//        }
    }

}