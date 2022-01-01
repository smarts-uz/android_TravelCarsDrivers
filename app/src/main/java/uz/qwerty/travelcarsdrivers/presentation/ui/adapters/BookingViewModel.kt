package uz.qwerty.travelcarsdrivers.presentation.ui.adapters

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import uz.qwerty.travelcarsdrivers.domain.repository.booking.NetworkState
import uz.qwerty.travelcarsdrivers.domain.repository.booking.BookingDataFactory
import uz.qwerty.travelcarsdrivers.domain.repository.booking.PageKeyedBookingDataSource
import uz.qwerty.travelcarsdrivers.domain.models.Booking
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class BookingViewModel(var pageSize: Int = 10) : ViewModel() {

    var buildingList: LiveData<PagedList<Booking>>
    var networkState: LiveData<NetworkState>
    var executor: Executor = Executors.newFixedThreadPool(5)!!
    var tDataSource: LiveData<PageKeyedBookingDataSource>
    var buildingDataSourceFactory: BookingDataFactory

    init {
        buildingDataSourceFactory = BookingDataFactory(executor)
        tDataSource = buildingDataSourceFactory.mutableLiveData
        networkState = Transformations.switchMap(
            buildingDataSourceFactory.mutableLiveData
        ) {
            it.networkState
        }
        val pagedListConfig = PagedList.Config.Builder()
            .setInitialLoadSizeHint(pageSize)
            .setEnablePlaceholders(false)
            .setPageSize(pageSize).build()
        buildingList = LivePagedListBuilder(buildingDataSourceFactory, pagedListConfig)
            .build()
    }
}