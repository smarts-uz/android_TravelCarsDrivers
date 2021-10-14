package uz.qwerty.travelcarsdrivers.repository.booking

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import uz.qwerty.travelcarsdrivers.vo.Booking
import java.util.concurrent.Executor

class BookingDataFactory(val executor: Executor) : DataSource.Factory<Long, Booking>() {

    lateinit var pageKeyedBuildingDataSource: PageKeyedBookingDataSource
    var mutableLiveData = MutableLiveData<PageKeyedBookingDataSource>()

    var apiKey: String? = null
    var type: String? = null

    override fun create(): DataSource<Long, Booking> {
        pageKeyedBuildingDataSource = PageKeyedBookingDataSource(
            executor,
            apiKey,
            type
        )
        mutableLiveData.postValue(pageKeyedBuildingDataSource)
        return pageKeyedBuildingDataSource
    }

}