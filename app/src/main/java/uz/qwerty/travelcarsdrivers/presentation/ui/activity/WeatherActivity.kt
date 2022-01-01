package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import uz.qwerty.travelcarsdrivers.databinding.ActivityWeatherBinding
import uz.qwerty.travelcarsdrivers.presentation.ui.base.BaseActivity
@AndroidEntryPoint
class WeatherActivity : BaseActivity<ActivityWeatherBinding>() {

    override fun bindingActivity(): ActivityWeatherBinding {
        return ActivityWeatherBinding.inflate(layoutInflater)
    }

    override fun onCreated(savedInstanceState: Bundle?) {

    }

}