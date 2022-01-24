package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.content_calendar.*
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.domain.models.Route
import uz.qwerty.travelcarsdrivers.presentation.ui.adapters.NewRouteAdapter
import uz.qwerty.travelcarsdrivers.presentation.ui.adapters.OnItemClickRv
import uz.qwerty.travelcarsdrivers.data.remote.api.TravelCarsApi
import uz.qwerty.travelcarsdrivers.presentation.ui.adapters.RouteAdapter
import uz.qwerty.travelcarsdrivers.util.OnStartChecks
import java.util.*

class CalendarActivity : AppCompatActivity() {

    var apiService = TravelCarsApi.createService(true)
    lateinit var newRouteAdapter: NewRouteAdapter
    lateinit var routeAdapter: RouteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)


        val sharedPref =
            this.getSharedPreferences(getString(R.string.config), Context.MODE_PRIVATE) ?: return
        val authKey = sharedPref.getString(getString(R.string.auth_key), null)

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
            } else {
                getProfile(authKey)
            }
        }


        routeAdapter = RouteAdapter()
        newRouteAdapter = NewRouteAdapter(object : OnItemClickRv {
            override fun clickItem(data: Route) {

                routesList.adapter = routeAdapter

            }

        })

        val linearLM = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        routesList.layoutManager = linearLM


        routesList.adapter = newRouteAdapter

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        nav_view.selectedItemId = R.id.navigation_stats

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, TripsActivity::class.java)
            intent.putExtra("type", "active")
            startActivity(intent)
        }

        calendarView.firstDayOfWeek = Calendar.MONDAY
        calendarView.minDate = Calendar.getInstance().timeInMillis

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            routes_progressbar.visibility = View.VISIBLE
            no_routes_label.visibility = View.INVISIBLE
            routesListView.visibility = View.INVISIBLE
            getRoutes(
                authKey,
                year.toString() + "-" + (month + 1).toString() + "-" + dayOfMonth.toString()
            )
        }

    }


    private fun getRoutes(apiKey: String?, date: String?) {
        apiService.routes(apiKey, date)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccessful && it.code() == 200) {

                    val data0 = it.body()!!.data
                    //routeAdapter.setBanners(it.body()!!.data)
                    newRouteAdapter.setBanners(data0[0])

                    routeAdapter.setBanners(data0)

                    routes_progressbar.visibility = View.INVISIBLE
                    if (it.body()!!.data.isEmpty()) {
                        no_routes_label.visibility = View.VISIBLE
                    } else {
                        routesListView.visibility = View.VISIBLE
                    }
                } else {
                    //TODO response message
                }
            }, {
                //TODO can't response
            })
    }


    override fun onRestart() {
        super.onRestart()
        nav_view.selectedItemId = R.id.navigation_stats
    }

    override fun onResume() {
        super.onResume()
        nav_view.selectedItemId = R.id.navigation_stats
    }

    private fun getProfile(apiKey: String?) {
        apiService.profile(apiKey).subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccessful && it.code() == 200) {
                    getRoutes(apiKey, null)
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
                 *   R.id.navigation_clock -> {
                val intent = Intent(this, TripsActivity::class.java)
                intent.putExtra("type", "review")
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
                }
                 *
                 */
                R.id.navigation_clock -> {
                    val intent = Intent(this, ServiceActivity::class.java)
                    intent.putExtra("type", "review")
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
//                val intent = Intent(this, SettingsActivity::class.java)
//                startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

}
