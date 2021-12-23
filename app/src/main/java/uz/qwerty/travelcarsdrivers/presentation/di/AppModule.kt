package uz.qwerty.travelcarsdrivers.presentation.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.qwerty.travelcarsdrivers.presentation.app.App
import javax.inject.Singleton


/**
 * Created by Abdurashidov Shahzod on 23/12/21 17:44.
 * company
 * shahzod9933@gmail.com
 */

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext app: Context): App = app as App
}