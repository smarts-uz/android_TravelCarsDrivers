package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import ru.lexcorp.autoscrollviewpager.AutoScrollViewPager
import ru.lexcorp.viewpagerindicator.CirclePageIndicator
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.util.TravelCarsApi
import uz.qwerty.travelcarsdrivers.presentation.ui.adapters.BookingActiveAdapter
import uz.qwerty.travelcarsdrivers.presentation.ui.adapters.BookingBannerAdapter
import uz.qwerty.travelcarsdrivers.util.OnStartChecks
import uz.qwerty.travelcarsdrivers.domain.models.User
import uz.qwerty.travelcarsdrivers.presentation.ui.common.course.CourseViewModel
import uz.qwerty.travelcarsdrivers.presentation.ui.state.Fail
import uz.qwerty.travelcarsdrivers.presentation.ui.state.Loading
import uz.qwerty.travelcarsdrivers.presentation.ui.state.ServerError
import uz.qwerty.travelcarsdrivers.presentation.ui.state.Success

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var apiService = TravelCarsApi.createService(true)
    private var doubleBackToExitPressedOnce = false
    private lateinit var user: User


    lateinit var viewPager: AutoScrollViewPager
    lateinit var bannerAdapter: BookingBannerAdapter
    lateinit var bannerIndicator: CirclePageIndicator

    lateinit var activeAdapter: BookingActiveAdapter

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_clock -> {
                    val intent = Intent(this, TripsActivity::class.java)
                    intent.putExtra("type", "review")
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

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref =
            this.getSharedPreferences(getString(R.string.config), Context.MODE_PRIVATE) ?: return
        val authKey = sharedPref.getString(getString(R.string.auth_key), null)
        val pin = sharedPref.getString(getString(R.string.pin_key), null)
        val pin_status = sharedPref.getString(getString(R.string.pin_status), null)

        bannerAdapter = BookingBannerAdapter()
        viewPager = findViewById(R.id.banner_list)
        bannerIndicator = findViewById(R.id.banner_indicator)
        viewPager.adapter = bannerAdapter
        currencyBtn.setOnClickListener {
            startActivity(Intent(this,CourseActivity::class.java))
        }
        weatherBtn.setOnClickListener {
            startActivity(Intent(this,WeatherActivity::class.java))
        }

        activeAdapter = BookingActiveAdapter()

        val linearLM = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        active_trips_list.layoutManager = linearLM
        active_trips_list.adapter = activeAdapter

        OnStartChecks.hasInternetConnection().subscribe { hasInternet ->
            if (!hasInternet) {
                val intent = Intent(this, NoConnectionActivity::class.java)
                startActivity(intent)
                finish()
                return@subscribe
            } else if (authKey.isNullOrBlank()) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                return@subscribe
            } else if (!pin.isNullOrBlank() && pin_status.isNullOrBlank()) {
                val intent = Intent(this, PinActivity::class.java)
                startActivity(intent)
                finish()
                return@subscribe
            } else {
                getProfile(authKey)
            }
        }

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        nav_view.selectedItemId = R.id.navigation_home

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, TripsActivity::class.java)
            intent.putExtra("type", "active")
            startActivity(intent)
        }

        counts_active.setOnClickListener {
            val intent = Intent(this, TripsActivity::class.java)
            intent.putExtra("type", "active")
            startActivity(intent)
        }

        counts_done.setOnClickListener {
            val intent = Intent(this, TripsActivity::class.java)
            intent.putExtra("type", "done")
            startActivity(intent)
        }

        counts_proceed.setOnClickListener {
            val intent = Intent(this, TripsActivity::class.java)
            intent.putExtra("type", "proceed")
            startActivity(intent)
        }

//        counts_rejected.setOnClickListener {
//            val intent = Intent(this, TripsActivity::class.java)
//            intent.putExtra("type", "rejected")
//            startActivity(intent)
//        }
    }



    override fun onRestart() {
        super.onRestart()
        nav_view.selectedItemId = R.id.navigation_home
    }

    private fun getProfile(apiKey: String?) {
        apiService.profile(apiKey).subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccessful && it.code() == 200) {
                    user = it.body()!!.data
                    getBanners(apiKey)
                    getActives(apiKey)
                    getCounts(apiKey)
                    setTitle(user.name)
                    return@subscribe
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@subscribe
                }
            }, {
                val intent = Intent(this, NoConnectionActivity::class.java)
                startActivity(intent)
                finish()
                return@subscribe
            })
    }

    private fun setTitle(name: String?) {
        toolbar_title.text = name
    }

    private fun getActives(apiKey: String?) {
        apiService.lastActive(apiKey)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccessful && it.code() == 200) {
                    activeAdapter.setBanners(it.body()!!.data)
                    activeTripsProgress.visibility = View.INVISIBLE
                    if (it.body()!!.data.isEmpty()) {
                        no_active_trips.visibility = View.VISIBLE
                    } else {
                        active_trips_list.visibility = View.VISIBLE
                    }
                } else {
                    //TODO response message
                }
            }, {
                //TODO can't response
            })
    }

    private fun getBanners(apiKey: String?) {
        apiService.tripsBanners(apiKey)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccessful && it.code() == 200) {
                    bannerAdapter.setBanners(it.body()!!.data)
                    viewPager.interval = 5000
                    viewPager.startAutoScroll()
                    bannerProgress.visibility = View.INVISIBLE
                    if (it.body()!!.data.isEmpty()) {
                        noNewTrips.visibility = View.VISIBLE
                    } else {
                        banner_list.visibility = View.VISIBLE
                    }
                    bannerIndicator.setViewPager(viewPager)
                } else {
                    //TODO response message
                }
            }, {
                //TODO can't response
            })
    }

    private fun getCounts(apiKey: String?) {
        apiService.counts(apiKey)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccessful && it.code() == 200) {
                    counts_active.text =
                        counts_active.text.toString() + " " + it.body()!!.data.active.toString()
                    counts_done.text =
                        counts_done.text.toString() + " " + it.body()!!.data.done.toString()
//                    counts_rejected.text = counts_rejected.text.toString() + " " +  it.body()!!.data.rejected.toString()
                    counts_proceed.text =
                        counts_proceed.text.toString() + " " + it.body()!!.data.proceed.toString()
                } else {
                    counts_active.text = counts_active.text.toString() + " 0"
                    counts_done.text = counts_done.text.toString() + " 0"
//                    counts_rejected.text = counts_rejected.text.toString() + " 0"
                    counts_proceed.text = counts_proceed.text.toString() + " 0"
                }
            }, {
                counts_active.text = counts_active.text.toString() + " 0"
                counts_done.text = counts_done.text.toString() + " 0"
//                counts_rejected.text = counts_rejected.text.toString() + " 0"
                counts_proceed.text = counts_proceed.text.toString() + " 0"
            })
    }

    override fun onPause() {
        super.onPause()
        // stop auto scroll when onPause
        viewPager.stopAutoScroll()
    }

    override fun onResume() {
        super.onResume()
        nav_view.selectedItemId = R.id.navigation_home
        // start auto scroll when onResume
        viewPager.startAutoScroll()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()

            val sharedPref =
                this.getSharedPreferences(getString(R.string.config), Context.MODE_PRIVATE)
                    ?: return
            val editor = sharedPref.edit()
            editor.putString(getString(R.string.pin_status), null)
            editor.apply()
            intent
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            finishAffinity()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Нажмите назад еще раз чтобы выйти", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

//    override fun onStop() {
//        super.onStop()
//
//        val sharedPref = this.getSharedPreferences(getString(R.string.config), Context.MODE_PRIVATE) ?: return
//        val editor = sharedPref.edit()
//        editor.putString(getString(R.string.pin_status), null)
//        editor.apply()
//    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPref =
            this.getSharedPreferences(getString(R.string.config), Context.MODE_PRIVATE) ?: return
        val editor = sharedPref.edit()
        editor.putString(getString(R.string.pin_status), null)
        editor.apply()
    }
}
