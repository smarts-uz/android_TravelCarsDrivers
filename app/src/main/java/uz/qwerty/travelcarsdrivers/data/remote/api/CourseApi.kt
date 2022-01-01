package uz.qwerty.travelcarsdrivers.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CurrencyItem
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CurrencyResponse
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CurrencyResponseItem


/**
 * Created by Abdurashidov Shahzod on 24/12/21 21:35.
 * company QQBank
 * shahzod9933@gmail.com
 */

interface CourseApi {
    @GET("json/")
    suspend fun getCourse(): Response<List<CurrencyResponseItem>>

    @GET("json/")
    suspend fun getAllCourse(): Response<CurrencyResponse>

    @GET("json/")
    suspend fun getCurrency(): Response<List<CurrencyItem>>

}