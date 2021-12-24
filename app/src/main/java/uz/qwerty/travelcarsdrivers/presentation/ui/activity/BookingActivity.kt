package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_booking.*
import kotlinx.android.synthetic.main.activity_booking.tabBar
import kotlinx.android.synthetic.main.content_booking.*
import uz.qwerty.travelcarsdrivers.R

import uz.qwerty.travelcarsdrivers.util.TravelCarsApi
import uz.qwerty.travelcarsdrivers.util.OnStartChecks
import uz.qwerty.travelcarsdrivers.domain.models.User

class BookingActivity : AppCompatActivity() {

    var id = 0
    var apiService = TravelCarsApi.createService(true)
    private lateinit var user: User
    var phone : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        fab.hide()

        val sharedPref = this.getSharedPreferences(getString(R.string.config), Context.MODE_PRIVATE) ?: return
        val authKey = sharedPref.getString(getString(R.string.auth_key), null)

        id = intent.getIntExtra("id", 0)


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

        fab.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel: $phone")
            startActivity(callIntent)
        }
        tabBar.addOnTabSelectedListener(tabSelectedListener())

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    inner class tabSelectedListener : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
        override fun onTabReselected(p0: TabLayout.Tab?) {
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
        }

        override fun onTabSelected(p0: TabLayout.Tab?) {
            when (p0!!.position) {
                1 -> {
                    client_view.visibility = View.GONE
                    route_view.visibility = View.VISIBLE
                }
                else -> {
                    client_view.visibility = View.VISIBLE
                    route_view.visibility = View.GONE
                }
            }
        }

    }

    private fun getProfile(apiKey: String?) {
        apiService.profile(apiKey).subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccessful && it.code() == 200) {
                    user = it.body()!!.data
                    getBooking(apiKey)
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

    private fun getBooking(apiKey: String?) {
        val api = apiService.show(id, apiKey)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccessful && it.code() == 200) {
                    city_from.text = it.body()!!.data.city_from
                    var cityToText = it.body()!!.data.city_to
                    if (it.body()!!.data.reverse == 1) {
                        cityToText += (" - " + it.body()!!.data.city_from)
                    }
                    city_to.text = cityToText
                    car_name.text = it.body()!!.data.car + " (" + it.body()!!.data.car_number + ")"
                    client_name.text = it.body()!!.data.user_name
                    price.text = it.body()!!.data.price
                    if (it.body()!!.data.paid.isNullOrBlank()) {
                        status.text = getString(R.string.not_paid)
                    } else {
                        status.text = getString(R.string.paid)
                    }

                    point_a.text = it.body()!!.data.city_from + ", " + it.body()!!.data.date
                    var pointBText = it.body()!!.data.city_to
                    if (it.body()!!.data.reverse == 1) {
                        pointBText += (", " + it.body()!!.data.date_reverse)
                        point_c.text = it.body()!!.data.city_from

                        point_c_label.visibility = View.VISIBLE
                        point_c.visibility = View.VISIBLE
                        divider7.visibility = View.VISIBLE
                    }
                    point_b.text = pointBText
                    if (it.body()!!.data.additional.isNullOrBlank()) {
                        comment.text = getString(R.string.no_comment)
                    } else {
                        comment.text = it.body()!!.data.additional
                    }

                    if (!it.body()!!.data.user_phone.isNullOrBlank()) {
                        phone = it.body()!!.data.user_phone
                        fab.show()
                    }

                    if (!it.body()!!.data.comment.isNullOrBlank()) {
                        review.text = it.body()!!.data.comment.toString()
                        review_label.visibility = View.VISIBLE
                        review.visibility = View.VISIBLE
                        divider10.visibility = View.VISIBLE
                    }

                    if (!it.body()!!.data.rating.isNullOrBlank()) {
                        rating.text = getString(R.string.rating) + ": " + it.body()!!.data.rating.toString()
                        review_label.visibility = View.VISIBLE
                        rating.visibility = View.VISIBLE
                        divider10.visibility = View.VISIBLE
                    }

                } else {
                    //TODO response message
                }
            }, {
                //TODO can't response
            })
    }


}
