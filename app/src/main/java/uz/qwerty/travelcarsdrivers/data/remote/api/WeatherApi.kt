package uz.qwerty.travelcarsdrivers.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import uz.qwerty.travelcarsdrivers.data.remote.response.weather.WeatherResponse
import uz.qwerty.travelcarsdrivers.util.config.Config.WEATHER_END_POINT


/**
 * Created by Abdurashidov Shahzod on 01/01/22 21:33.
 * company QQBank
 * shahzod9933@gmail.com
 */
interface WeatherApi {
    @GET(WEATHER_END_POINT)
    suspend fun getWeather(): Response<WeatherResponse>
}