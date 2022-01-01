package uz.qwerty.travelcarsdrivers.domain.repository.weather

import kotlinx.coroutines.flow.Flow
import uz.qwerty.travelcarsdrivers.data.remote.response.weather.WeatherResponse


/**
 * Created by Abdurashidov Shahzod on 01/01/22 21:42.
 * company QQBank
 * shahzod9933@gmail.com
 */
interface WeatherRepository {
    fun getWeather():Flow<Result<WeatherResponse>>
}