package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_service.*
import kotlinx.android.synthetic.main.activity_trips.*
import kotlinx.android.synthetic.main.activity_trips.floatingActionButton
import kotlinx.android.synthetic.main.activity_trips.toolbar_title
import kotlinx.android.synthetic.main.activity_trips.trip_nav_view
import kotlinx.android.synthetic.main.content_profile.*
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.data.remote.api.TravelCarsApi
import uz.qwerty.travelcarsdrivers.databinding.ActivityServiceBinding
import uz.qwerty.travelcarsdrivers.domain.models.Booking
import uz.qwerty.travelcarsdrivers.domain.models.NewCurrencyResponse
import uz.qwerty.travelcarsdrivers.domain.models.User
import uz.qwerty.travelcarsdrivers.domain.repository.booking.NetworkState
import uz.qwerty.travelcarsdrivers.presentation.ui.adapters.BookingAdapter
import uz.qwerty.travelcarsdrivers.presentation.ui.adapters.BookingViewModel
import uz.qwerty.travelcarsdrivers.presentation.ui.adapters.CourseAdapter
import uz.qwerty.travelcarsdrivers.presentation.ui.base.BaseActivity
import uz.qwerty.travelcarsdrivers.presentation.ui.common.course.CourseViewModel
import uz.qwerty.travelcarsdrivers.presentation.ui.common.weather.WeatherActivityVM
import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.gone
import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.visible
import uz.qwerty.travelcarsdrivers.presentation.ui.state.*
import uz.qwerty.travelcarsdrivers.util.OnStartChecks
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ServiceActivity : BaseActivity<ActivityServiceBinding>() {
    private val courseAdapter by lazy { CourseAdapter() }
    private val vm: CourseViewModel by viewModels()
    private val vmWeather: WeatherActivityVM by viewModels()

    var apiService = TravelCarsApi.createService(true)
    private lateinit var user: User
    private lateinit var type: String
    private lateinit var viewModel: BookingViewModel
    private lateinit var bookingAdapter: BookingAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreated(savedInstanceState: Bundle?) {
        loadObserver()
        loadView()
        loadWeatherObserver()

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

        floatingActionButton.setOnClickListener {
//            if (tabBar.selectedTabPosition != 1) {
//                bookingAdapter.review = false
//                toolbar_title.text = getString(R.string.service)
//                tabBar.visibility = View.VISIBLE
//                type = "active"
//                trip_nav_view.selectedItemId = R.id.navigation_invisible
//                tabBar.getTabAt(1)!!.select()
//            }
        }
    }

    private fun loadView() {
        rvCurrency.adapter = courseAdapter
        vm.getCourse()
    }

    private fun loadObserver() {
        vm.currencyNewLiveData.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                render(it)
            }
        }
    }

    private fun render(viewState: ViewState) {

        if (viewState !is Loading) {
            binding.currencyProgressBar.gone()
        }
        when (viewState) {
            is Fail -> {
                showErrorMessage(getString(R.string.warning), viewState.exception.message!!)
            }
            is Loading -> {
                //binding.swipeRefreshLayout.isRefreshing = true
                binding.currencyProgressBar.visible()
            }
            is ServerError -> {
                showErrorMessage(getString(R.string.warning), viewState.errorMessage)
            }

            is Success<*> -> {
                val list = viewState.data as List<NewCurrencyResponse>
                val newList = ArrayList<NewCurrencyResponse>()
                for (i in list.indices) {
                    if (list[i].code == "USD" ||
                        list[i].code == "EUR" ||
                        list[i].code == "RUB"
                    ) {
                        val newCurrencyResponse = list[i]
                        newList.addAll(listOf(newCurrencyResponse))
                    }
                }
                courseAdapter.submitList(newList)

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

                /**
                 *
                 * R.id.navigation_clock -> {
                if (type != "review") {
                val intent = Intent(this, TripsActivity::class.java)
                intent.putExtra("type", "review")
                startActivity(intent)
                }
                return@OnNavigationItemSelectedListener true
                }
                 */
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

            val sCalendar = Calendar.getInstance()
            val day = sCalendar.get(Calendar.DAY_OF_MONTH)
            val sdf = SimpleDateFormat("EEEE")
            val d = Date()
            val dayOfTheWeek: String = sdf.format(d)

            //binding.date.text = "$dayOfTheWeek $day"
            binding.date.text = "$currentDay.$currentMonth.$currentYear"
            binding.humidity.text = "${weatherResponse.main.humidity}%"
            binding.wind.text = "${weatherResponse.wind.speed}м/с"
            binding.min.text = "${weatherResponse.main.tempMin}°"
            binding.max.text = "${weatherResponse.main.tempMax}°"
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