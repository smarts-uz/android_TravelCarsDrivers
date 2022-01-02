package uz.qwerty.travelcarsdrivers.data.repository.currency

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.data.remote.api.CourseApi
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CurrencyItem
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
            val course = api.getNewCurrency()
            if (course.isSuccessful) {
                Success(course.body())
            } else {
                ServerError(course.message(), course.code())
            }
        } catch (e: Exception) {
            Fail(e)
        }
    }

    override fun getCurrency(): Flow<Result<List<CurrencyItem>>> = flow {
        try {
            val responseCourse = api.getCurrency()
            if (responseCourse.code() == 200) responseCourse.body()?.let {
                emit(Result.success(it))
            } else {
                emit(Result.failure<List<CurrencyItem>>(Exception(App.instance.getString(R.string.error))))
            }
        } catch (e: Exception) {
            Timber.e("XATOLIK = $e")
            emit(Result.failure<List<CurrencyItem>>(e))
        }
    }

    override suspend fun getNewCourse(): ViewState {
        return try {
            val response = api.getAllCourse()
            if (response.isSuccessful) {
                Success(response.body())
            } else {
                ServerError(response.message(), response.code())
            }
        } catch (e: Exception) {
            Fail(e)
        }
    }

}