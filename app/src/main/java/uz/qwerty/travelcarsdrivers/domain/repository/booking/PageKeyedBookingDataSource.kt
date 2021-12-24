package uz.qwerty.travelcarsdrivers.domain.repository.booking

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.qwerty.travelcarsdrivers.util.TravelCarsApi
import uz.qwerty.travelcarsdrivers.domain.models.Booking
import java.util.concurrent.Executor

class PageKeyedBookingDataSource(
    retryExecutor: Executor,
    var apiKey: String? = null,
    var type: String? = "done"
) : PageKeyedDataSource<Long, Booking>() {

    var bookingService = TravelCarsApi.createService(true)
    var initialParams: PageKeyedDataSource.LoadInitialParams<Long>? = null
    var afterParams: PageKeyedDataSource.LoadParams<Long>? = null

    var networkState = MutableLiveData<NetworkState>()
    var initialLoading = MutableLiveData<NetworkState>()

    var page: Long? = 1

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Booking>) {
        val bookings = ArrayList<Booking>()
        initialParams = params

        initialLoading.postValue(NetworkState.LOADING)
        networkState.postValue(NetworkState.LOADING)
        bookingService.history(
            apiKey,
            type,
            page).subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe {
//            }
            .subscribe({
                if (it.isSuccessful && it.code() == 200) {
                    page = if (page!! >= it.body()!!.meta.last_page!!) {
                        null
                    } else {
                        page!! + 1
                    }
                    bookings.addAll(it.body()!!.data)
                    callback.onResult(bookings, null, page)
                    initialParams = null

                    if (bookings.isNullOrEmpty()) {
                        initialLoading.postValue(NetworkState.EMPTY)
                        networkState.postValue(NetworkState.EMPTY)
                    } else {
                        initialLoading.postValue(NetworkState.LOADED)
                        networkState.postValue(NetworkState.LOADED)
                    }
                } else {
                    page = null
                    initialLoading.postValue(NetworkState(Status.FAILED, it.message()))
                    networkState.postValue(NetworkState(Status.FAILED, it.message()))
                    //TODO response message
                }
            }, {
                var errorMessage: String?
                errorMessage = it.message
                if (errorMessage == null) {
                    errorMessage = "unknown error"
                }
                networkState.postValue(NetworkState(Status.FAILED, errorMessage))
                //TODO can't response
            })
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Booking>) {
        val bookings = ArrayList<Booking>()
        afterParams = params

        networkState.postValue(NetworkState.LOADING)
        bookingService.history(
            apiKey,
            type,
            page).subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe {
//            }
            .subscribe({
                if (it.isSuccessful && it.code() == 200) {
                    page = if (page!! >= it.body()!!.meta.last_page!!) {
                        null
                    } else {
                        page!! + 1
                    }
                    bookings.addAll(it.body()!!.data)
                    callback.onResult(bookings, page)
                    networkState.postValue(NetworkState.LOADED)
                    initialParams = null
                } else {
                    page = null
                    networkState.postValue(NetworkState(Status.FAILED, it.message()))
                    //TODO response message
                }
            }, {
                var errorMessage: String?
                errorMessage = it.message
                if (errorMessage == null) {
                    errorMessage = "unknown error"
                }
                networkState.postValue(NetworkState(Status.FAILED, errorMessage))
                //TODO can't response
            })
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Booking>) {}
}