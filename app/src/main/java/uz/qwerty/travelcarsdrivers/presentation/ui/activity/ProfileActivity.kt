package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_profile.floatingActionButton
import kotlinx.android.synthetic.main.activity_profile.nav_view
import kotlinx.android.synthetic.main.activity_profile.toolbar_title
import kotlinx.android.synthetic.main.content_profile.*
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.util.TravelCarsApi
import uz.qwerty.travelcarsdrivers.util.OnStartChecks
import uz.qwerty.travelcarsdrivers.domain.models.User





class ProfileActivity : AppCompatActivity() {


    var apiService = TravelCarsApi.createService(true)
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val sharedPref = this.getSharedPreferences(getString(R.string.config), Context.MODE_PRIVATE) ?: return
        val authKey = sharedPref.getString(getString(R.string.auth_key), null)
        val pin = sharedPref.getString(getString(R.string.pin_key), null)

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

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        nav_view.selectedItemId = R.id.navigation_dashboard

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, TripsActivity::class.java)
            intent.putExtra("type", "active")
            startActivity(intent)
        }


        if (!pin.isNullOrBlank()) {
            current_pin_layout.visibility = View.VISIBLE
            save_button.text = getString(R.string.edit)

            current_pin.addTextChangedListener {
                save_button.isEnabled = !it.isNullOrBlank() && !confirm_new_pin.text.isNullOrBlank()&& !new_pin.text.isNullOrBlank()
            }
            new_pin.addTextChangedListener {
                save_button.isEnabled = !it.isNullOrBlank() && !confirm_new_pin.text.isNullOrBlank() && !current_pin.text.isNullOrBlank()
            }
            confirm_new_pin.addTextChangedListener {
                save_button.isEnabled = !it.isNullOrBlank() && !new_pin.text.isNullOrBlank() && !current_pin.text.isNullOrBlank()
            }

            save_button.setOnClickListener{
                save_button.visibility = View.GONE
                save_progress.visibility = View.VISIBLE
                if (current_pin.text.toString() != pin) {
                    val toast = Toast.makeText(applicationContext, getString(R.string.current_pin_wrong), Toast.LENGTH_LONG)
                    toast.show()
                } else if (new_pin.text.toString() != confirm_new_pin.text.toString()) {
                    val toast = Toast.makeText(applicationContext, getString(R.string.pin_not_confirm), Toast.LENGTH_LONG)
                    toast.show()
                } else if (new_pin.text.toString().trim().length < 4) {
                    new_pin.error = getString(R.string.pin_error_min)
                } else {
                    val editor = sharedPref.edit()
                    editor.putString(getString(R.string.pin_key), new_pin.text.toString())
                    editor.putString(getString(R.string.pin_status), "success")
                    editor.apply()
                    val toast = Toast.makeText(applicationContext, getString(R.string.pin_set), Toast.LENGTH_LONG)
                    toast.show()
                }
                save_progress.visibility = View.GONE
                save_button.visibility = View.VISIBLE
            }
        } else {
            new_pin.addTextChangedListener {
                save_button.isEnabled = !it.isNullOrBlank() && !confirm_new_pin.text.isNullOrBlank()
            }
            confirm_new_pin.addTextChangedListener {
                save_button.isEnabled = !it.isNullOrBlank() && !new_pin.text.isNullOrBlank()
            }

            save_button.setOnClickListener{
                save_button.visibility = View.GONE
                save_progress.visibility = View.VISIBLE
                if (new_pin.text.toString() != confirm_new_pin.text.toString()) {
                    val toast = Toast.makeText(applicationContext, getString(R.string.pin_not_confirm), Toast.LENGTH_LONG)
                    toast.show()
                } else if (new_pin.text.toString().trim().length < 4) {
                    new_pin.error = getString(R.string.pin_error_min)
                } else {
                    val editor = sharedPref.edit()
                    editor.putString(getString(R.string.pin_key), new_pin.text.toString())
                    editor.putString(getString(R.string.pin_status), "success")
                    editor.apply()
                    val toast = Toast.makeText(applicationContext, getString(R.string.pin_set), Toast.LENGTH_LONG)
                    toast.show()
                }
                save_progress.visibility = View.GONE
                save_button.visibility = View.VISIBLE
            }
        }


        current_password.addTextChangedListener {
            edit_button.isEnabled = !it.isNullOrBlank() && !new_password.text.isNullOrBlank() && !confirm_new_password.text.isNullOrBlank()
        }
        new_password.addTextChangedListener {
            edit_button.isEnabled = !it.isNullOrBlank() && !current_password.text.isNullOrBlank() && !confirm_new_password.text.isNullOrBlank()
        }
        confirm_new_password.addTextChangedListener {
            edit_button.isEnabled = !it.isNullOrBlank() && !current_password.text.isNullOrBlank()&& !new_password.text.isNullOrBlank()
        }

        edit_button.setOnClickListener {
            edit_button.visibility = View.GONE
            edit_progress.visibility = View.VISIBLE
            if (new_password.text.toString().trim().length < 6) {
                new_password.error = getString(R.string.password_error_min)
            } else {
                updateProfile(authKey, current_password.text.toString(), new_password.text.toString(), confirm_new_password.text.toString())
            }
        }


        logout_button.setOnClickListener {
            apiService.logout(authKey).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isSuccessful && it.code() == 200) {
                        val editor = sharedPref.edit()
                        editor.putString(getString(R.string.auth_key), null)
                        editor.putString(getString(R.string.pin_key), null)
                        editor.apply()
                        val intent = Intent(this, LoginActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                        return@subscribe
                    } else {
                        val toast =
                            Toast.makeText(applicationContext, getString(R.string.request_error), Toast.LENGTH_LONG)
                        toast.show()
                    }
                }, {
                    val toast =
                        Toast.makeText(applicationContext, getString(R.string.request_error), Toast.LENGTH_LONG)
                    toast.show()

                })
        }
    }


    private fun updateProfile(apiKey: String?, current_password: String?, new_password: String?, confirm_new_password: String? ) {
        apiService.profileUpdate(apiKey, current_password, new_password, confirm_new_password).subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccessful && it.code() == 200) {
                    val toast = Toast.makeText(applicationContext, it.body()!!.result_message, Toast.LENGTH_LONG)
                    toast.show()
                    edit_progress.visibility = View.GONE
                    edit_button.visibility = View.VISIBLE
                    return@subscribe
                } else {
                    val toast = Toast.makeText(applicationContext, it.body()!!.result_message, Toast.LENGTH_LONG)
                    toast.show()
                    edit_progress.visibility = View.GONE
                    edit_button.visibility = View.VISIBLE
                    return@subscribe
                }
            }, {
                val toast =
                    Toast.makeText(applicationContext, getString(R.string.request_error), Toast.LENGTH_LONG)
                toast.show()
                edit_progress.visibility = View.GONE
                edit_button.visibility = View.VISIBLE
                return@subscribe
            })
    }

    override fun onRestart() {
        super.onRestart()
        nav_view.selectedItemId = R.id.navigation_dashboard
    }

    override fun onResume() {
        super.onResume()
        nav_view.selectedItemId = R.id.navigation_dashboard
    }

    private fun getProfile(apiKey: String?) {
        apiService.profile(apiKey).subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccessful && it.code() == 200) {
                    user = it.body()!!.data
                    setTitle(user.name)
                    setData()
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

    private fun setTitle(name: String?)
    {
        toolbar_title.text = name
    }

    private fun setData()
    {
        user_name.text = user.name
//        user_licence.text = user.licence_number
        user_email.text = user.username
        if (!user.phone.isNullOrBlank()) {
            user_phone.text = user.phone
        }
//        if (!user.birthday.isNullOrBlank()) {
//            user_birthday.text = user.birthday
//        }
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
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
}
