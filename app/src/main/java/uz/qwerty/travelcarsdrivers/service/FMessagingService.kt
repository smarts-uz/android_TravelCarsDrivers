package uz.qwerty.travelcarsdrivers.service


import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import uz.qwerty.travelcarsdrivers.R

class FMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        // Check if message contains a data payload.
        if (remoteMessage!!.data.isNotEmpty()) {

        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    override fun onNewToken(s: String?) {
        super.onNewToken(s)
        val sharedPref = this.getSharedPreferences(getString(R.string.config), Context.MODE_PRIVATE) ?: return
        val editor = sharedPref.edit()

        editor.putString(getString(R.string.fcmToken), s)
        editor.apply()
    }
}
