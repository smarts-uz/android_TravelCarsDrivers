package uz.qwerty.travelcarsdrivers.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import uz.qwerty.travelcarsdrivers.data.remote.api.CourseApi
import uz.qwerty.travelcarsdrivers.data.remote.api.WeatherApi
import javax.inject.Named
import javax.inject.Singleton


/**
 * Created by Abdurashidov Shahzod on 24/12/21 21:27.
 * company QQBank
 * shahzod9933@gmail.com
 */
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @[Provides Singleton]
    fun provideCourseApi(@Named("Currency") retrofit: Retrofit): CourseApi =
        retrofit.create(CourseApi::class.java)

//    @[Provides Singleton]
//    fun provideNewCourseApi(@Named("NewCurrency") retrofit: Retrofit): CourseApi =
//        retrofit.create(CourseApi::class.java)


    @[Provides Singleton]
    fun provideWeatherApi(@Named("Weather") retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)
}