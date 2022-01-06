package uz.qwerty.travelcarsdrivers.util.service


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.presentation.ui.activity.MainActivity

class FMessagingService : FirebaseMessagingService() {


    fun generateNotification(title: String?, message: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val channelId = "Travel"
        val channelName = "uz.qwerty.travelcarsdrivers.util.service"
        var builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        applicationContext.resources,
                        R.mipmap.ic_launcher
                    )
                )
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setColor(Color.parseColor("#394ac9"))
                .setContentIntent(pendingIntent)
        builder = builder.setContent(getRemoteView(title, message))

        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.setSound(null, null)
            manager.createNotificationChannel(channel)
            manager.notify(0, builder.build())
        }


    }

    private fun getRemoteView(title: String?, message: String): RemoteViews? {
        val remoteView =
            RemoteViews("uz.qwerty.travelcarsdrivers.util.service", R.layout.notification)
        remoteView.setTextViewText(R.id.txtTitle, title)
        remoteView.setTextViewText(R.id.txtDesc, message)
        remoteView.setImageViewResource(R.id.appLogo, R.drawable.launch_screen)
        return remoteView
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage?) {


        if (remoteMessage!!.data.isNotEmpty()) {

        }

        if (remoteMessage.notification != null) {
            generateNotification(
                remoteMessage.notification?.title,
                remoteMessage.notification!!.body!!
            )
        }
    }

    override fun onNewToken(s: String?) {
        super.onNewToken(s)
        val sharedPref =
            this.getSharedPreferences(getString(R.string.config), Context.MODE_PRIVATE) ?: return
        val editor = sharedPref.edit()

        editor.putString(getString(R.string.fcmToken), s)
        editor.apply()
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(
        title: String?,
        text: String?,
        action: String?,
        sound: String?
    ) {


        //val fullLink = AUDIO_LINK + sound?.replace("_",".")
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("TravelPush", action)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val channelId = "Travel"
        val builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        applicationContext.resources,
                        R.mipmap.ic_launcher
                    )
                )
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setColor(Color.parseColor("#394ac9"))
                .setContentIntent(pendingIntent)


//        if (sound == null){
//            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//        } else {
//
//        }
        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "travel_channel",
                NotificationManager.IMPORTANCE_LOW
            )
            channel.setSound(null, null)
            manager.createNotificationChannel(channel)
        }


    }

    private fun playSound(fullLink: String) {
        try {
            val ringtone: Ringtone =
                RingtoneManager.getRingtone(applicationContext, Uri.parse(fullLink))
            ringtone.audioAttributes =
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(5)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT).build()
            ringtone.play()
        } catch (e: java.lang.Exception) {
            Log.d("DETROIT", "error play sound $e")
        }
    }


}
