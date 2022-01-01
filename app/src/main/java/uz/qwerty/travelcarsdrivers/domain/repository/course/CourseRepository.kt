package uz.qwerty.travelcarsdrivers.domain.repository.course

import kotlinx.coroutines.flow.Flow
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CurrencyItem
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CurrencyResponseItem
import uz.qwerty.travelcarsdrivers.presentation.ui.state.ViewState


/**
 * Created by Abdurashidov Shahzod on 24/12/21 21:46.
 * company QQBank
 * shahzod9933@gmail.com
 */

interface CourseRepository {
    suspend fun getCourse(): ViewState
    fun getCurrency(): Flow<Result<List<CurrencyItem>>>
    suspend fun getNewCourse(): ViewState
}