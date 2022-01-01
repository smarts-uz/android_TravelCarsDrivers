package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CourseResponseItem
import uz.qwerty.travelcarsdrivers.presentation.ui.common.course.CourseViewModel
import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.showToast
import uz.qwerty.travelcarsdrivers.presentation.ui.state.Fail
import uz.qwerty.travelcarsdrivers.presentation.ui.state.Loading
import uz.qwerty.travelcarsdrivers.presentation.ui.state.ServerError
import uz.qwerty.travelcarsdrivers.presentation.ui.state.Success
import uz.qwerty.travelcarsdrivers.util.TravelCarsApi
import uz.qwerty.travelcarsdrivers.util.OnStartChecks
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val vm: CourseViewModel by viewModels()
    lateinit var login: EditText
    private lateinit var password: EditText
    private lateinit var button: Button
    lateinit var progressBar: ProgressBar

    var apiService = TravelCarsApi.createService(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        addObservers()
        OnStartChecks.hasInternetConnection().subscribe { hasInternet ->
            if (!hasInternet) {
                val intent = Intent(this, NoConnectionActivity::class.java)
                startActivity(intent)
                finish()
                return@subscribe
            }
        }

        val sharedPref = this.getSharedPreferences(getString(R.string.config), Context.MODE_PRIVATE) ?: return
        val fcmToken = sharedPref.getString(getString(R.string.fcmToken), null)

        login = findViewById(R.id.editText)
        password = findViewById(R.id.editText2)
        button = findViewById(R.id.login_button)
        button.isEnabled = false
        progressBar = findViewById(R.id.progressBar)

        login.addTextChangedListener {
            button.isEnabled = !it.isNullOrBlank() && !password.text.isNullOrBlank()
        }
        password.addTextChangedListener {
            button.isEnabled = !it.isNullOrBlank() && !login.text.isNullOrBlank()
        }

        button.setOnClickListener { btn ->
            btn.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            apiService.login(login.text.toString(), password.text.toString(), fcmToken).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isSuccessful && it.code() == 200) {
                        val editor = sharedPref.edit()
                        editor.putString(getString(R.string.auth_key), it.body()!!.data.api_token)
                        editor.apply()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        return@subscribe
                    } else {
                        val toast =
                            Toast.makeText(applicationContext, getString(R.string.error_auth), Toast.LENGTH_LONG)
                        toast.show()
                    }
                    login.requestFocus()
                    btn.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE
                }, {
                    val toast =
                        Toast.makeText(applicationContext, getString(R.string.request_error), Toast.LENGTH_LONG)
                    toast.show()
                    login.requestFocus()
                    btn.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE
                })
        }

    }
    private fun addObservers() {
        vm.courseLiveData.observe(this, Observer {
            when (it) {
                is Fail -> {
                    showToast("Fail -> ${it.exception.message}")
                }
                is ServerError -> {
                }
                is Loading -> {}
                is Success<*> -> {
                    showToast("Success!!!")
//                    val data = it.data as List<CourseResponseItem>
//                    showToast(data[0].CcyNm_UZC)
                }
                else -> Unit

            }
        })
//        lifecycleScope.launchWhenStarted {
//            vm.courseState.collect {
//                when (it) {
//                    is Fail -> {
//                        //showToast()
//                    }
//                    is ServerError -> {
//                        //showToast()
//                    }
//                    is Loading -> {}
//                    is Success<*> -> {
//                        val courseAll = it.data as CourseResponse
//                        showToast(courseAll.toString())
//                    }
//                    else -> Unit
//
//                }
//            }
//        }
    }
}
