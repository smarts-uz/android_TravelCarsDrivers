package uz.qwerty.travelcarsdrivers.presentation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.qwerty.travelcarsdrivers.domain.repository.course.CourseRepository
import uz.qwerty.travelcarsdrivers.data.repository.CourseRepositoryImpl


/**
 * Created by Abdurashidov Shahzod on 24/12/21 22:14.
 * company
 * shahzod9933@gmail.com
 */
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindCourseRepository(courseRepositoryImpl: CourseRepositoryImpl): CourseRepository
}