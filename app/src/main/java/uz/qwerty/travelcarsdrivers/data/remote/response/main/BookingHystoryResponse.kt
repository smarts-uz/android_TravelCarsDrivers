package uz.qwerty.travelcarsdrivers.data.remote.response.main

import uz.qwerty.travelcarsdrivers.domain.models.Booking
import uz.qwerty.travelcarsdrivers.domain.models.Meta

class BookingHystoryResponse {
    lateinit var data: List<Booking>
    lateinit var meta: Meta
}