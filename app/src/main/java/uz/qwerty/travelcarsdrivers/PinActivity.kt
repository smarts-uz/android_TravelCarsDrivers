package uz.qwerty.travelcarsdrivers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.addTextChangedListener

import kotlinx.android.synthetic.main.activity_pin.*


class PinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        val sharedPref = this.getSharedPreferences(getString(R.string.config), Context.MODE_PRIVATE) ?: return
        val pin = sharedPref.getString(getString(R.string.pin_key), null)
        val pin_status = sharedPref.getString(getString(R.string.pin_status), null)

        pin_code.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        pin_code.addTextChangedListener {
            if (pin_code.text.toString().trim().length == 4) {
               if ( pin_code.text.toString().trim() == pin) {
                   val editor = sharedPref.edit()
                   editor.putString(getString(R.string.pin_status), "success")
                   editor.apply()
                   val intent = Intent(this, MainActivity::class.java)
                   startActivity(intent)
                   finish()
               } else {
                   val toast = Toast.makeText(applicationContext, getString(R.string.wrong_input), Toast.LENGTH_LONG)
                   toast.show()
                   pin_code.text.clear()
               }
            }
        }

    }

}
