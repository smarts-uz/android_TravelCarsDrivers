package uz.qwerty.travelcarsdrivers.presentation.di

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.qwerty.travelcarsdrivers.util.config.Config.VALYUT
import uz.qwerty.travelcarsdrivers.util.config.Config.WEATHER_URL
import uz.qwerty.travelcarsdrivers.util.isConnected
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


private const val MAX_STALE = 60 * 60 * 24 * 30 // 30day

private const val cacheSize: Long = 30 * 1024 * 1024 // 30MB
//private val cache = Cache(App.instance.cacheDir, cacheSize)

/**
 * Created by Abdurashidov Shahzod on 24/12/21 20:14.
 * company QQBank
 * shahzod9933@gmail.com
 */
@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {


    @Provides
    @Singleton
    fun getApi(): String = VALYUT

    @Provides
    @Singleton
    fun getLogging(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun okHttpClient(
        logging: HttpLoggingInterceptor,
        @ApplicationContext context: Context,
        //storage: LocalStorage
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        //.cache(cache)
        .addInterceptor(provideOfflineCacheInterceptor())
        //.addInterceptor(ChuckInterceptor(context))//for seeing responses and requests from emulator
        .addInterceptor {
            val requestOld = it.request()
            val request = requestOld.newBuilder()
                .removeHeader("Authorization")//additional
                //.addHeader("Authorization", "Bearer " + storage.token)
                .method(requestOld.method, requestOld.body)
                .build()
            val response = it.proceed(request)
            response
        }
        .build()

    @Provides
    @Singleton
    @Named("Currency")
    fun getCurrencyRetrofit(
        api: String,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(api)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @Named("Weather")
    fun getWeatherRetrofit(
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(WEATHER_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //retrieves max-stale cache if connection is off
    private fun provideOfflineCacheInterceptor() = Interceptor { chain ->
        var request = chain.request()

        if (!isConnected()) {
            val cacheControl = CacheControl.Builder()
                .maxStale(MAX_STALE, TimeUnit.SECONDS)
                .build()

            request = request.newBuilder()
                .removeHeader("Cache-Control")
                .cacheControl(cacheControl)
                .build()
        }
        chain.proceed(request)
    }

}