package uz.qwerty.travelcarsdrivers.presentation.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import uz.qwerty.travelcarsdrivers.BuildConfig


/**
 * Created by Abdurashidov Shahzod on 23/12/21 15:37.
 * company
 * shahzod9933@gmail.com
 */

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        instance = this
        super.onCreate()

    }

    companion object {
        lateinit var instance: App
    }
}