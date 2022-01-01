package uz.qwerty.travelcarsdrivers.presentation.ui.common.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import uz.qwerty.travelcarsdrivers.data.remote.response.weather.WeatherResponse
import uz.qwerty.travelcarsdrivers.domain.repository.weather.WeatherRepository
import uz.qwerty.travelcarsdrivers.presentation.ui.base.BaseVM
import javax.inject.Inject


/**
 * Created by Abdurashidov Shahzod on 01/01/22 21:15.
 * company QQBank
 * shahzod9933@gmail.com
 */
@HiltViewModel
class WeatherActivityVM @Inject constructor(
    private val repository: WeatherRepository
) : BaseVM() {
    private var _weatherLiveData = MutableLiveData<WeatherResponse>()
    val weatherLiveData: LiveData<WeatherResponse> get() = _weatherLiveData

    private var _weatherLiveDataError = MutableLiveData<String>()
    val weatherLiveDataError: LiveData<String> get() = _weatherLiveDataError

    init {
        getWeather()
    }

    private fun getWeather() {
        launchViewModel(Dispatchers.IO) {
            repository.getWeather().collect { it ->
                it.onSuccess{
                    _weatherLiveData.postValue(it)
                    Timber.tag("zarnigor").d("Zarnigor malumot keldi")
                }
                it.onFailure {
                    _weatherLiveDataError.postValue(it.message)
                    Timber.tag("zarnigor").d("Zarnigor fail ->${it.message}")
                }
            }
        }
    }
}