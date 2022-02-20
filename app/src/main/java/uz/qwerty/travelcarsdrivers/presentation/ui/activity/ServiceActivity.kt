package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_trips.floatingActionButton
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.databinding.ActivityServiceBinding
import uz.qwerty.travelcarsdrivers.domain.models.NewCurrencyResponse
import uz.qwerty.travelcarsdrivers.presentation.ui.base.BaseActivity
import uz.qwerty.travelcarsdrivers.presentation.ui.common.course.CourseViewModel
import uz.qwerty.travelcarsdrivers.presentation.ui.common.weather.WeatherActivityVM
import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.gone
import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.visible
import uz.qwerty.travelcarsdrivers.presentation.ui.state.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ServiceActivity : BaseActivity<ActivityServiceBinding>() {
    private val vm: CourseViewModel by viewModels()
    private val vmWeather: WeatherActivityVM by viewModels()
    private lateinit var type: String
    private lateinit var simpleDateFormat:SimpleDateFormat
    private lateinit var simpleDateFormatH:SimpleDateFormat
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreated(savedInstanceState: Bundle?) {
        loadObserver()
        loadView()
        loadWeatherObserver()
        simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
        simpleDateFormatH = SimpleDateFormat("HH:mm")
        type = intent.getStringExtra("type")


        type = intent.getStringExtra("type")


//        binding.navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
//        binding.navView.selectedItemId = if (type == "review") {
//            R.id.navigation_clock
//        } else {
//            R.id.navigation_invisible
//        }

        binding.navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        binding.navView.selectedItemId = R.id.navigation_clock

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, TripsActivity::class.java)
            intent.putExtra("type", "active")
            startActivity(intent)
        }


        floatingActionButton.setOnClickListener {
            val intent = Intent(this, TripsActivity::class.java)
            intent.putExtra("type", "active")
            startActivity(intent)
        }
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, TripsActivity::class.java)
            intent.putExtra("type", "active")
            startActivity(intent)
        }

    }

    private fun loadView() {
//        rvCurrency.adapter = courseAdapter
//        vm.getCourse()
    }

    private fun loadObserver() {
        vm.currencyNewLiveData.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                render(it)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun render(viewState: ViewState) {

        if (viewState !is Loading) {
            //binding.currencyProgressBar.gone()
        }
        when (viewState) {
            is Fail -> {
                showErrorMessage(getString(R.string.warning), viewState.exception.message!!)
            }
            is Loading -> {
                //binding.swipeRefreshLayout.isRefreshing = true
                //binding.currencyProgressBar.visible()
            }
            is ServerError -> {
                showErrorMessage(getString(R.string.warning), viewState.errorMessage)
            }

            is Success<*> -> {
                val list = viewState.data as List<NewCurrencyResponse>
                val newList = ArrayList<NewCurrencyResponse>()
                for (i in list.indices) {
                    if (list[i].code == "USD"
                    ) {
                        val newCurrencyResponse = list[i]
                        binding.cbUsd.text = list[i].cbPrice
                        binding.buyUsd.text = list[i].nbuBuyPrice
                        binding.weSellUsd.text = list[i].nbuCellPrice
                        newList.addAll(listOf(newCurrencyResponse))
                    }
                    if (list[i].code == "EUR"
                    ) {
                        val newCurrencyResponse = list[i]
                        binding.cb.text = list[i].cbPrice
                        binding.buy.text = list[i].nbuBuyPrice
                        binding.weSell.text = list[i].nbuCellPrice
                        newList.addAll(listOf(newCurrencyResponse))
                    }
                    if (list[i].code == "RUB"
                    ) {
                        val newCurrencyResponse = list[i]
                        binding.cbRubl.text = list[i].cbPrice
                        binding.buyRubl.text = list[i].nbuBuyPrice
                        binding.weSellRubl.text = list[i].nbuCellPrice
                        binding.timeUsd.text = simpleDateFormat.format(Date())
                        binding.time.text = simpleDateFormat.format(Date())
                        binding.timeRubl.text = simpleDateFormat.format(Date())
                        binding.timeWeather.text = simpleDateFormatH.format(Date()) + " Режим УЗТ"
                        newList.addAll(listOf(newCurrencyResponse))
                    }
                }

            }
            else -> {}
        }
    }


    override fun onRestart() {
        super.onRestart()
        binding.navView.selectedItemId =
            R.id.navigation_clock

    }

    override fun onResume() {
        super.onResume()
        binding.navView.selectedItemId =
            R.id.navigation_clock

    }



    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }

               R.id.navigation_clock -> {
                if (type != "review") {
                val intent = Intent(this, TripsActivity::class.java)
                intent.putExtra("type", "review")
                startActivity(intent)
                }
                return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_clock -> {
                    if (type != "review") {
                        val intent = Intent(this, ServiceActivity::class.java)
                        intent.putExtra("type", "review")
                        startActivity(intent)
                    }
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_invisible -> {
                    val intent = Intent(this, TripsActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_stats -> {
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun bindingActivity(): ActivityServiceBinding {
        return ActivityServiceBinding.inflate(layoutInflater)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadWeatherObserver() {
        vmWeather.weatherLiveData.observe(this) {
            val weatherResponse = it
            binding.temp.text = "${weatherResponse.main.temp.toInt()}°"
            val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
            val currentMonth = SimpleDateFormat("MM", Locale.getDefault()).format(Date())
            val currentDay = SimpleDateFormat("dd", Locale.getDefault()).format(Date())

            binding.date.text = "$currentDay.$currentMonth.$currentYear"
            binding.humidity.text = "${weatherResponse.main.humidity}%"
            binding.wind.text = "${weatherResponse.wind.speed}м/с"
            binding.min.text = "${weatherResponse.main.tempMin.toInt()}°"
            binding.max.text = "${weatherResponse.main.tempMax.toInt()}°"
            binding.pressure.text = weatherResponse.main.pressure.toString() + " МБ"

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