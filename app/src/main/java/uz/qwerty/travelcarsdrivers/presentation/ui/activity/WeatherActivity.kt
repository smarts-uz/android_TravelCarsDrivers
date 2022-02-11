package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.databinding.ActivityWeatherBinding
import uz.qwerty.travelcarsdrivers.presentation.ui.base.BaseActivity
import uz.qwerty.travelcarsdrivers.presentation.ui.common.weather.WeatherActivityVM
import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.gone
import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.visible
import java.text.DateFormat.LONG
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.LONG
import java.util.TimeZone.LONG

@AndroidEntryPoint
class WeatherActivity : BaseActivity<ActivityWeatherBinding>() {

    private val vm: WeatherActivityVM by viewModels()
    override fun bindingActivity(): ActivityWeatherBinding {
        return ActivityWeatherBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreated(savedInstanceState: Bundle?) {
        loadObserver()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadObserver() {
        vm.weatherLiveData.observe(this) {
            val weatherResponse = it
            binding.temp.text = "${weatherResponse.main.temp.toInt()}°"
            val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
            val currentMonth = SimpleDateFormat("MM", Locale.getDefault()).format(Date())
            val currentDay = SimpleDateFormat("dd", Locale.getDefault()).format(Date())

            var sCalendar = Calendar.getInstance()
            val day = sCalendar.get(Calendar.DAY_OF_MONTH)
            val sdf = SimpleDateFormat("EEEE")
            val d = Date()
            val dayOfTheWeek: String = sdf.format(d)

            //binding.date.text = "$dayOfTheWeek $day"
            binding.date.text = "$currentDay.$currentMonth.$currentYear"
            binding.humidity.text = "${weatherResponse.main.humidity}%"
            binding.wind.text = "${weatherResponse.wind.speed}м/с"
            binding.min.text = "${weatherResponse.main.tempMin}%"
            binding.max.text = "${weatherResponse.main.tempMax}%"
            binding.pressure.text = weatherResponse.main.pressure.toString()

            binding.progressBar.gone()
        }
        vm.loadingLiveData.observe(this) {
            binding.progressBar.visible()
        }

        vm.errorLiveData.observe(this) {
            showErrorMessage(getString(R.string.warning), it)
        }
        vm.connectLiveData.observe(this) {
            if (it) showErrorMessage(
                getString(R.string.warning),
                getString(R.string.no_connection_icon)
            )
        }
    }

}

