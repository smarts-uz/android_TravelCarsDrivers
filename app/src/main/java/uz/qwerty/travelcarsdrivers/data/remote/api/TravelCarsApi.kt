package uz.qwerty.travelcarsdrivers.data.remote.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import uz.qwerty.travelcarsdrivers.util.config.Config.baseUrl

object TravelCarsApi {

    fun createService(logging: Boolean): TravelCarsService {

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
        if (logging) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            retrofit.client(client)
        }

        retrofit.addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.build().create(TravelCarsService::class.java)
    }
}