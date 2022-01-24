package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_trips.*
import kotlinx.android.synthetic.main.activity_trips.floatingActionButton
import kotlinx.android.synthetic.main.activity_trips.toolbar_title
import kotlinx.android.synthetic.main.content_profile.*
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.data.remote.api.TravelCarsApi
import uz.qwerty.travelcarsdrivers.domain.models.Booking
import uz.qwerty.travelcarsdrivers.domain.models.User
import uz.qwerty.travelcarsdrivers.domain.repository.booking.NetworkState
import uz.qwerty.travelcarsdrivers.presentation.ui.adapters.BookingAdapter
import uz.qwerty.travelcarsdrivers.presentation.ui.adapters.BookingViewModel
import uz.qwerty.travelcarsdrivers.util.OnStartChecks

class ServiceActivity : AppCompatActivity() {



    var apiService = TravelCarsApi.createService(true)
    private lateinit var user: User
    private lateinit var type: String
    private lateinit var viewModel: BookingViewModel
    private lateinit var bookingAdapter: BookingAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        currencyBtn.setOnClickListener {
            startActivity(Intent(this,CourseActivity::class.java))
        }
        weatherBtn.setOnClickListener {
            startActivity(Intent(this,WeatherActivity::class.java))
        }
        type = intent.getStringExtra("type")


        trip_nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        trip_nav_view.selectedItemId = if (type == "review") {
            R.id.navigation_clock
        } else {
            R.id.navigation_invisible
        }


        floatingActionButton.setOnClickListener {
            if (tabBar.selectedTabPosition != 1) {
                bookingAdapter.review = false
                toolbar_title.text = getString(R.string.service)
                tabBar.visibility = View.VISIBLE
                type = "active"
                trip_nav_view.selectedItemId = R.id.navigation_invisible
                tabBar.getTabAt(1)!!.select()
            }
        }
    }


    override fun onRestart() {
        super.onRestart()
        trip_nav_view.selectedItemId = if (type == "review") {
            R.id.navigation_clock
        } else {
            R.id.navigation_invisible
        }
    }

    override fun onResume() {
        super.onResume()
        trip_nav_view.selectedItemId = if (type == "review") {
            R.id.navigation_clock
        } else {
            R.id.navigation_invisible
        }
    }



    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
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

}