package uz.qwerty.travelcarsdrivers.domain.repository.booking

import uz.qwerty.travelcarsdrivers.util.Status

class NetworkState(val sts: Status, val mes: String) {
    companion object {
        var LOADED: NetworkState = NetworkState(Status.SUCCESS, "Success")
        var LOADING: NetworkState = NetworkState(Status.RUNNING, "Running")
        var EMPTY: NetworkState = NetworkState(Status.EMPTY, "Нет заявок")
    }
}