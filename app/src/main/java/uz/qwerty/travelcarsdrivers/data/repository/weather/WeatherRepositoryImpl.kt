package uz.qwerty.travelcarsdrivers.data.repository.weather

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import uz.qwerty.travelcarsdrivers.data.remote.api.WeatherApi
import uz.qwerty.travelcarsdrivers.data.remote.response.weather.WeatherResponse
import uz.qwerty.travelcarsdrivers.domain.repository.weather.WeatherRepository
import javax.inject.Inject


/**
 * Created by Abdurashidov Shahzod on 01/01/22 21:44.
 * company QQBank
 * shahzod9933@gmail.com
 */
class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {
    override fun getWeather(): Flow<Result<WeatherResponse>> = flow {
        try {
            val response = api.getWeather()
            if (response.code() == 200) response.body()?.let {
                emit(Result.success<WeatherResponse>(it))
            } else {
                emit(Result.failure<WeatherResponse>(Exception("error")))
            }
        } catch (e: Exception) {
            Timber.e("Xatolik = $e")
            emit(Result.failure<WeatherResponse>(e))
        }
    }
}