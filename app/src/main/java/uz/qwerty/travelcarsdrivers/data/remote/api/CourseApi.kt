package uz.qwerty.travelcarsdrivers.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import uz.qwerty.travelcarsdrivers.data.remote.api.baseresponse.BaseResponse
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CourseResponse
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CourseResponseItem


/**
 * Created by Abdurashidov Shahzod on 24/12/21 21:35.
 * company QQBank
 * shahzod9933@gmail.com
 */

interface CourseApi {
    @GET("json/")
    suspend fun getCourse(): BaseResponse<List<CourseResponseItem>>
}