package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_trips.*
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.data.remote.api.TravelCarsApi
import uz.qwerty.travelcarsdrivers.domain.repository.booking.NetworkState
import uz.qwerty.travelcarsdrivers.presentation.ui.adapters.BookingAdapter
import uz.qwerty.travelcarsdrivers.presentation.ui.adapters.BookingViewModel
import uz.qwerty.travelcarsdrivers.util.ListItemClickListener
import uz.qwerty.travelcarsdrivers.util.OnStartChecks
import uz.qwerty.travelcarsdrivers.domain.models.Booking
import uz.qwerty.travelcarsdrivers.domain.models.User


class TripsActivity : AppCompatActivity(), ListItemClickListener {

    override fun onRetryClick(view: View, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClick(id: Int) {
        val intent = Intent(this, BookingActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    var apiService = TravelCarsApi.createService(true)
    private lateinit var user: User
    private lateinit var type: String
    private lateinit var viewModel: BookingViewModel
    private lateinit var bookingAdapter: BookingAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)

        val sharedPref = this.getSharedPreferences(getString(R.string.config), Context.MODE_PRIVATE) ?: return
        val authKey = sharedPref.getString(getString(R.string.auth_key), null)

        type = intent.getStringExtra("type")

        viewModel = ViewModelProviders.of(this).get(BookingViewModel::class.java)
        bookingAdapter = BookingAdapter(this)
        val llm = LinearLayoutManager(this)
        llm.orientation = RecyclerView.VERTICAL
        booking_list.layoutManager = llm
        booking_list.adapter = bookingAdapter

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

        trip_nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        trip_nav_view.selectedItemId = if (type == "review") {
            R.id.navigation_clock
        } else {
            R.id.navigation_invisible
        }

        // Setup refresh listener which triggers new data loading
        activity_main_swipe.setOnRefreshListener {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            fetchTimelineAsync()
        }
        // Configure the refreshing colors

        activity_main_swipe.setColorSchemeResources(
            R.color.colorAccent
        )


        when (type) {
            "active" -> {
                bookingAdapter.review = false
                toolbar_title.text = getString(R.string.trips)
                tabBar.visibility = View.VISIBLE
                tabBar.getTabAt(1)!!.select()
            }
            "rejected" -> {
                bookingAdapter.review = false
                toolbar_title.text = getString(R.string.trips)
                tabBar.visibility = View.VISIBLE
                tabBar.getTabAt(3)!!.select()
            }
            "proceed" -> {
                bookingAdapter.review = false
                toolbar_title.text = getString(R.string.trips)
                tabBar.visibility = View.VISIBLE
                tabBar.getTabAt(2)!!.select()
            }
            "review" -> {
                bookingAdapter.review = true
                toolbar_title.text = getString(R.string.reviews)
                tabBar.getTabAt(0)!!.select()
                tabBar.visibility = View.GONE
            }
            else -> {
                bookingAdapter.review = false
                toolbar_title.text = getString(R.string.trips)
                tabBar.visibility = View.VISIBLE
                tabBar.getTabAt(0)!!.select()
            }
        }

        tabBar.addOnTabSelectedListener(tabSelectedListener())


        floatingActionButton.setOnClickListener {
            if (tabBar.selectedTabPosition != 1) {
                bookingAdapter.review = false
                toolbar_title.text = getString(R.string.trips)
                tabBar.visibility = View.VISIBLE
                type = "active"
                trip_nav_view.selectedItemId = R.id.navigation_invisible
                tabBar.getTabAt(1)!!.select()
                fetchTimelineAsync()
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

    inner class tabSelectedListener: TabLayout.BaseOnTabSelectedListener<TabLayout.Tab>{
        override fun onTabReselected(p0: TabLayout.Tab?) {
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
        }

        override fun onTabSelected(p0: TabLayout.Tab?) {
            when(p0!!.position) {
                1 -> {
                    type = "active"
                    fetchTimelineAsync()

                }
                3 -> {
                    type = "rejected"
                    fetchTimelineAsync()
                }
                2 -> {
                    type = "proceed"
                    fetchTimelineAsync()
                }
                else -> {
                    type = "done"
                    fetchTimelineAsync()
                }
            }
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

    private fun getProfile(apiKey: String?) {
        apiService.profile(apiKey).subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccessful && it.code() == 200) {
                    user = it.body()!!.data
                    getTrips(apiKey)
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


    private fun fetchTimelineAsync() {
        viewModel.buildingDataSourceFactory.type = type
        viewModel.buildingDataSourceFactory.mutableLiveData.value!!.invalidate()
    }

    private fun getTrips(apiKey: String?) {
        viewModel.buildingDataSourceFactory.apiKey = apiKey
        viewModel.buildingDataSourceFactory.type = type

        viewModel.buildingList.observe(this, Observer<PagedList<Booking>> {
            bookingAdapter.submitList(it)
            activity_main_swipe.isRefreshing = false
        })
        viewModel.networkState.observe(this, Observer<NetworkState> {
            bookingAdapter.setNetworkState(it!!)
        })
    }
}
