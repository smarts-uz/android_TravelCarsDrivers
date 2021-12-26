package uz.qwerty.travelcarsdrivers.domain.repository.course

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import timber.log.Timber
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.data.remote.api.CourseApi
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CourseResponse
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
            if (course.isSuccessful) {
                Success(course.body())
            } else {
                ServerError(course.message(), course.code())
            }
        } catch (e: Exception) {
            Fail(e)
        }
    }

    override fun getCourseAll(): Flow<Result<CourseResponse>> = flow {
        try {
            val responseCourse = api.getCourse()
            if (responseCourse.code() == 200) responseCourse.body()?.let {
                emit(Result.success(it))
            } else {
                emit(Result.failure<CourseResponse>(Exception(App.instance.getString(R.string.error))))
            }
        } catch (e: Exception) {
            Timber.e("Repository in function refreshToken error = $e")
            emit(Result.failure<CourseResponse>(e))
        }
    }

}