package uz.qwerty.travelcarsdrivers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import uz.qwerty.travelcarsdrivers.util.OnStartChecks

class NoConnectionActivity : AppCompatActivity() {

    private lateinit var retryButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)

        retryButton = findViewById(R.id.retry_button)
        progressBar = findViewById(R.id.progressBar)

        retryButton.setOnClickListener {
            it.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            OnStartChecks.hasInternetConnection().subscribe { hasInternet ->
                if (hasInternet) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@subscribe
                }
                it.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            }
        }
    }
}
