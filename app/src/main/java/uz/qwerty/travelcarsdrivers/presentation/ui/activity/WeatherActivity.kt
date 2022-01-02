package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.databinding.ActivityWeatherBinding
import uz.qwerty.travelcarsdrivers.presentation.ui.base.BaseActivity
import uz.qwerty.travelcarsdrivers.presentation.ui.common.weather.WeatherActivityVM

@AndroidEntryPoint
class WeatherActivity : BaseActivity<ActivityWeatherBinding>() {

    private val vm: WeatherActivityVM by viewModels()
    override fun bindingActivity(): ActivityWeatherBinding {
        return ActivityWeatherBinding.inflate(layoutInflater)
    }

    override fun onCreated(savedInstanceState: Bundle?) {
        loadObserver()
    }

    private fun loadObserver() {
        vm.weatherLiveData.observe(this, {
            val weatherResponse = it
            binding.temp.text = "+${weatherResponse.main.temp}°"
            binding.nameCountry.text = weatherResponse.name
            val dayOfWeek = Calendar.DAY_OF_WEEK
            binding.date.text = dayOfWeek.toString()
            binding.humidity.text = "${weatherResponse.main.humidity}%"
            binding.wind.text = "${weatherResponse.wind.speed}m/s"
            binding.pressure.text = weatherResponse.main.pressure.toString()
        })
        vm.errorLiveData.observe(this, {
            showErrorMessage(getString(R.string.warning), it)
        })
        vm.connectLiveData.observe(this,{
            if (it) showErrorMessage(getString(R.string.warning),getString(R.string.no_connection_icon))
        })
    }

}